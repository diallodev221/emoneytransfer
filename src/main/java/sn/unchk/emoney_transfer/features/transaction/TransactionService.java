package sn.unchk.emoney_transfer.features.transaction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sn.unchk.emoney_transfer.enums.StatusTransaction;
import sn.unchk.emoney_transfer.enums.TypeTransaction;
import sn.unchk.emoney_transfer.features.compte.Compte;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static sn.unchk.emoney_transfer.enums.StatusTransaction.TERMINE;
import static sn.unchk.emoney_transfer.enums.TypeTransaction.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CompteRepository compteRepo;
    private final TransactionRepository transactionRepo;
    private final UtilisateurService utilisateurService;
    private final TransactionMapper mapper;


    @Transactional
    public void effectuerTransaction(Long sourceId, TransactionRequest request) {
        log.info("Début de la transaction de {} vers {}", sourceId, request.destinataireId());

        Compte compteSource = compteRepo.findById(sourceId).orElseThrow();
        Compte compteDestinataire = compteRepo.findById(request.destinataireId()).orElseThrow();

        if (compteSource.getSolde().compareTo(request.montant()) < 0) throw new RuntimeException("Solde insuffisant");

        double frais = calculateFees(request.montant(), 0.05);
        BigDecimal totalAmount = calculateTotalAmount(request.montant(), frais);

        compteSource.setSolde(compteSource.getSolde().subtract(request.montant()));
        compteDestinataire.setSolde(compteSource.getSolde().add(totalAmount));

        compteRepo.saveAll(List.of(compteSource, compteDestinataire));

        createTransactionForTransfer(compteSource, compteDestinataire, request.montant(), totalAmount, frais);

        log.info("Transaction de {} vers {} réussie", compteDestinataire.getUtilisateur().getNom(), compteDestinataire.getUtilisateur().getNom());
    }

    public void createTransactionFromAccount(Compte compte, BigDecimal montant, String type) {
        Transaction transaction = new Transaction();

        if (type.equals("credit")) {
            transaction = Transaction.builder()
                    .montant(montant)
                    .totalAmount(montant)
                    .date(LocalDateTime.now())
                    .description("Depot de  " + montant + " FCFA")
                    .source(compte)
                    .destinataire(null)
                    .date(LocalDateTime.now())
                    .typeTransaction(DEPOSIT)
                    .status(TERMINE)
                    .build();
        }
        if (type.equals("debit")) {
            transaction = Transaction.builder()
                    .montant(montant)
                    .totalAmount(montant)
                    .date(LocalDateTime.now())
                    .description("Retrait de  " + montant + " FCFA")
                    .source(compte)
                    .destinataire(null)
                    .date(LocalDateTime.now())
                    .typeTransaction(WITHDRAWAL)
                    .status(TERMINE)
                    .build();
        }

        transactionRepo.save(transaction);
    }

    public void createTransactionForTransfer(Compte compte, Compte compteDst, BigDecimal montant, BigDecimal totalAmount, double frais) {
        Transaction transactionDst = Transaction.builder()
                .montant(compte.getSolde())
                .description("Vous avez recu " + montant + " FCFA de " + compteDst.getUtilisateur().getPrenom())
                .source(compte)
                .destinataire(compteDst)
                .date(LocalDateTime.now())
                .typeTransaction(TRANSFER_SENT)
                .montant(totalAmount)
                .totalAmount(totalAmount)
                .frais(frais)
                .status(TERMINE)
                .date(LocalDateTime.now())
                .build();

        Transaction transactionSrc = Transaction.builder()
                .montant(compte.getSolde())
                .description("Vous avez envoyé " + montant + " FCFA a " + compteDst.getUtilisateur().getPrenom())
                .source(compte)
                .destinataire(compteDst)
                .date(LocalDateTime.now())
                .typeTransaction(TRANSFER_RECEIVED)
                .status(TERMINE)
                .montant(montant)
                .totalAmount(totalAmount)
                .date(LocalDateTime.now())
                .build();

        transactionRepo.saveAll(List.of(transactionDst, transactionSrc));
    }


    public List<TransactionResponse> getAllTransactions() {
        return transactionRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> getAllTransactionsCurrentUser() {
        Utilisateur currentUser = utilisateurService.getCurrentUser();
        Compte compte = compteRepo.findByUtilisateurId(currentUser.getId()).orElseThrow();

        if (Objects.nonNull(currentUser.getProfile()) && currentUser.getProfile().getName().equals("ADMIN")) {
            return transactionRepo.findAll().stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        return transactionRepo.getAllTransactions(compte.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> getTenRecentTransactions() {
        Utilisateur currentUser = utilisateurService.getCurrentUser();

        if (Objects.nonNull(currentUser.getProfile()) && currentUser.getProfile().getName().equals("ADMIN")) {
            return transactionRepo.findAll().stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        return transactionRepo.getTenRecentsTransactions(currentUser.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> getFilteredTransactions(TypeTransaction type, StatusTransaction status) {
        Utilisateur currentUser = utilisateurService.getCurrentUser();
        Specification<Transaction> transactionSpec = TransactionSpecification.filter(type, status, currentUser);
        if (Objects.nonNull(currentUser.getProfile()) && currentUser.getProfile().getName().equals("ADMIN")) {
            return transactionRepo.findAll().stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        return transactionRepo.findAll(transactionSpec).stream()
                .map(mapper::toResponse)
                .toList();
    }

    private double calculateFees(BigDecimal amount, double frais) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            return amount.doubleValue() * frais; // 0.5% de frais
        } else {
            return 0;
        }
    }

    private BigDecimal calculateTotalAmount(BigDecimal amount, double amountFee) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            return amount.add(BigDecimal.valueOf(amountFee)); // 0.5% de frais
        } else {
            return BigDecimal.ZERO;
        }
    }


}
