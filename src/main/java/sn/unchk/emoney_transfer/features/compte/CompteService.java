package sn.unchk.emoney_transfer.features.compte;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.features.transaction.TransactionService;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurRepository;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurService;
import sn.unchk.emoney_transfer.utils.CodeGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CompteService {

    private final CompteRepository compteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CompteMapper mapper;
    private final UtilisateurService utilisateurService;
    private final TransactionService transactionService;


    public CompteDto creerCompte(Long utilisateurId, BigDecimal soldeInitial) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Compte compte = Compte.builder()
                .solde(Objects.nonNull(soldeInitial) ? soldeInitial : BigDecimal.ZERO)
                .numeroCompte(CodeGenerator.generateBankAccountNumber(16))
                .active(true)
                .dateCreation(LocalDateTime.now())
                .utilisateur(utilisateur)
                .build();

        return mapper.toDto(compteRepository.save(compte));
    }

    @Transactional
    public CompteDto crediter(Long compteId, BigDecimal montant) {
        Compte compte = loadCompteById(compteId);
        compte.setSolde(compte.getSolde().add(montant));
        Compte saved = compteRepository.save(compte);

        transactionService.createTransactionFromAccount(saved, montant, "credit");

        return mapper.toDto(saved);
    }

    @Transactional
    public CompteDto debiter(Long compteId, BigDecimal montant) throws BadRequestException {
        Compte compte = loadCompteById(compteId);

        if (compte.getSolde().compareTo(montant) < 0) {
            throw new BadRequestException("Solde insuffisant");
        }

        compte.setSolde(compte.getSolde().subtract(montant));

        Compte saved = compteRepository.save(compte);

        transactionService.createTransactionFromAccount(saved, montant, "debit");

        return mapper.toDto(saved);
    }

    public Map<String, BigDecimal> consulterSolde(Long compteId) {
        return Map.of("solde", loadCompteById(compteId).getSolde());
    }

    public CompteDto desactiverCompte(Long compteId) {
        Compte compte = loadCompteById(compteId);
        compte.setActive(false);
        return mapper.toDto(compteRepository.save(compte));
    }

    public CompteDto reactiverCompte(Long compteId) {
        Compte compte = loadCompteById(compteId);
        compte.setActive(true);
        return mapper.toDto(compteRepository.save(compte));
    }

    public void supprimerCompte(Long compteId) {
        Compte compte = loadCompteById(compteId);
        compteRepository.delete(compte);
    }

    public CompteDto getComptesParUtilisateur(Long utilisateurId) {
        return compteRepository.findByUtilisateurId(utilisateurId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé pour l'utilisateur donné"));
    }

    public CompteDto getCompteById(Long id) {
        return compteRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
    }

    public List<CompteDto> getComptesActifs() {
        Utilisateur currentUser = utilisateurService.getCurrentUser();
        return compteRepository.findActiveComptes(currentUser.getId()).stream()
                .map(mapper::toDto)
                .toList();
    }

    private Compte loadCompteById(Long compteId) {
        return compteRepository.findById(compteId)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
    }

    public List<CompteDto> getComptes() {
        return compteRepository.findAllComptes().stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CompteDto> getComptesEnvoyees() {
        Utilisateur currentUser = utilisateurService.getCurrentUser();

        CompteDto comptesParUtilisateur = getComptesParUtilisateur(currentUser.getId());

        return compteRepository.findAllComptesPourEnvoie(comptesParUtilisateur.id()).stream()
                .map(mapper::toDto)
                .toList();
    }
}

