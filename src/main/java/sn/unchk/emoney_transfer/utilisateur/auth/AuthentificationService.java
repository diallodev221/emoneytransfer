package sn.unchk.emoney_transfer.utilisateur.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.compte.Compte;
import sn.unchk.emoney_transfer.compte.CompteRepository;
import sn.unchk.emoney_transfer.utilisateur.RegisterRequest;
import sn.unchk.emoney_transfer.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.utilisateur.UtilisateurRepository;
import sn.unchk.emoney_transfer.utilisateur.profile.Profile;
import sn.unchk.emoney_transfer.utilisateur.profile.ProfileRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthentificationService {

    private final UtilisateurRepository utilisateurRepository;
    private final ProfileRepository profileRepository;
    private final CompteRepository compteRepository;


    @Transactional
    public Utilisateur signup(RegisterRequest request) throws BadRequestException {
        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setPrenom(request.prenom());
        user.setNom(request.nom());
        user.setEmail(request.email());
        user.setMotDePasse(request.motDePasse());
        user.setTelephone(request.telephone());
        user.setPays(request.pays());

        Profile profile = profileRepository.findByName("UTILISATEUR")
                .orElseThrow(() -> new EntityNotFoundException("Profile 'UTILISATEUR' not found"));

        user.setProfile(profile);

        Utilisateur savedUser = utilisateurRepository.save(user);

        Compte compte = new Compte();
        compte.setUtilisateur(savedUser);
        compte.setSolde(BigDecimal.ZERO);
        compteRepository.save(compte);

        return savedUser;
    }


    public Utilisateur login(String email, String motDePasse) throws BadRequestException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        if (!utilisateur.getMotDePasse().equals(motDePasse)) {
            throw new BadRequestException("Mot de passe incorrect");
        }

        return utilisateur;
    }


}
