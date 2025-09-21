package sn.unchk.emoney_transfer.features.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.features.compte.Compte;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CompteRepository compteRepo;
    private final TransactionRepository transactionRepo;


    @Transactional
    public TransactionResponse effectuerTransaction(Long sourceId, TransactionRequest request) {
        log.info("Début de la transaction de {} vers {}", sourceId, request.destinataireId());

        Compte compteSource = compteRepo.findById(sourceId).orElseThrow();
        Compte compteDestinataire = compteRepo.findById(request.destinataireId()).orElseThrow();

        if (compteSource.getSolde().compareTo(request.montant()) < 0) throw new RuntimeException("Solde insuffisant");

        compteSource.setSolde(compteSource.getSolde().subtract(request.montant()));
        compteDestinataire.setSolde(compteSource.getSolde().add(request.montant()));

        compteRepo.saveAll(List.of(compteSource, compteDestinataire));


        Transaction transaction = new Transaction();
        transaction.setSource(compteSource);
        transaction.setDestinataire(compteDestinataire);
        transaction.setMontant(request.montant());
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(request.description());
        transactionRepo.save(transaction);

        log.info("Transaction de {} vers {} réussie", compteDestinataire.getUtilisateur().getNom(), compteDestinataire.getUtilisateur().getNom());
        return new TransactionResponse(compteSource.getUtilisateur().getNom(), compteDestinataire.getUtilisateur().getNom(), request.montant(), transaction.getDate(), transaction.getDescription());
    }
}
