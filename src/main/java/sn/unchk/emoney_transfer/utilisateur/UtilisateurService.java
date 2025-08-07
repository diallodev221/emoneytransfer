package sn.unchk.emoney_transfer.utilisateur;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.compte.Compte;
import sn.unchk.emoney_transfer.compte.CompteRepository;
import sn.unchk.emoney_transfer.utilisateur.profile.Profile;
import sn.unchk.emoney_transfer.utilisateur.profile.ProfileRepository;

import java.math.BigDecimal;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepo;
    private final CompteRepository compteRepo;
    private final ProfileRepository profileRepo;

    public UtilisateurService(UtilisateurRepository utilisateurRepo, CompteRepository compteRepo, ProfileRepository profileRepo) {
        this.utilisateurRepo = utilisateurRepo;
        this.compteRepo = compteRepo;
        this.profileRepo = profileRepo;
    }

    @Transactional
    public Utilisateur enregistrer(RegisterRequest request) {
        if (utilisateurRepo.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setPrenom(request.prenom());
        user.setNom(request.nom());
        user.setEmail(request.email());
        user.setMotDePasse(request.motDePasse());
        user.setTelephone(request.telephone());
        user.setPays(request.pays());

        Profile profile = profileRepo.findByName("UTILISATEUR")
                .orElseThrow(() -> new EntityNotFoundException("Profile 'UTILISATEUR' not found"));

        user.setProfile(profile);

        Utilisateur savedUser = utilisateurRepo.save(user);

        Compte compte = new Compte();
        compte.setUtilisateur(savedUser);
        compte.setSolde(BigDecimal.ZERO);
        compteRepo.save(compte);

        return savedUser;
    }


}
