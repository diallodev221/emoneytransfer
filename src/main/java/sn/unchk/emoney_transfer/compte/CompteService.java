package sn.unchk.emoney_transfer.compte;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import sn.unchk.emoney_transfer.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.utilisateur.UtilisateurRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CompteService {

    private final CompteRepository compteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CompteMapper mapper;



    public CompteDto creerCompte(Long utilisateurId, BigDecimal soldeInitial) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Compte compte = new Compte();
        compte.setUtilisateur(utilisateur);
        compte.setSolde(soldeInitial != null ? soldeInitial : BigDecimal.ZERO);
        compte.setActive(true);
        compte.setDateCreation(LocalDateTime.now());

        return mapper.toDto(compteRepository.save(compte));
    }

    public CompteDto crediter(Long compteId, BigDecimal montant) {
        Compte compte = loadCompteById(compteId);
        compte.setSolde(compte.getSolde().add(montant));
        return mapper.toDto(compteRepository.save(compte));
    }

    public CompteDto debiter(Long compteId, BigDecimal montant) throws BadRequestException {
        Compte compte = loadCompteById(compteId);

        if (compte.getSolde().compareTo(montant) < 0) {
            throw new BadRequestException("Solde insuffisant");
        }

        compte.setSolde(compte.getSolde().subtract(montant));
        return mapper.toDto(compteRepository.save(compte));
    }

    public Map<String, BigDecimal> consulterSolde(Long compteId) {
        return  Map.of("solde", loadCompteById(compteId).getSolde());
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

    public List<CompteDto> getComptesParUtilisateur(Long utilisateurId) {
        return compteRepository.findByUtilisateurId(utilisateurId).stream()
                .map(mapper::toDto)
                .toList();
    }

    // ✅ Récupérer un compte par ID
    public CompteDto getCompteById(Long id) {
        return compteRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
    }

    // ✅ Lister tous les comptes actifs
    public List<CompteDto> getComptesActifs() {
        return compteRepository.findByActiveTrue().stream()
                .map(mapper::toDto)
                .toList();
    }

    private Compte loadCompteById(Long compteId) {
        return compteRepository.findById(compteId)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
    }
}

