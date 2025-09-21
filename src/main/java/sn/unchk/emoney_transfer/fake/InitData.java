package sn.unchk.emoney_transfer.fake;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sn.unchk.emoney_transfer.features.compte.Compte;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurRepository;
import sn.unchk.emoney_transfer.features.utilisateur.profile.Profile;
import sn.unchk.emoney_transfer.features.utilisateur.profile.ProfileRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    /**
     * This class is used to initialize data when the application starts.
     * It implements CommandLineRunner to execute code after the application context is loaded.
     */

    private final CompteRepository compteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        // Log the initialization of data
        log.info("Initialisation des données...");


        if (profileRepository.findAll().isEmpty()) {
            // cree des profils par defaults
            Profile utilisateur = Profile.builder()
                    .name("UTILISATEUR")
                    .description("Profil par défaut pour les utilisateurs")
                    .build();

            Profile admin = Profile.builder()
                    .name("ADMIN")
                    .description("Profil par défaut pour les administrateurs")
                    .build();

            profileRepository.saveAll(List.of(utilisateur, admin));
        }

        if (utilisateurRepository.findAll().isEmpty()) {
            // cree des utilisateurs par defaults
            Utilisateur u1 = Utilisateur.builder()
                    .prenom("Alice")
                    .nom("Dupont")
                    .email("alice.dupont@email.com")
                    .motDePasse(passwordEncoder.encode("password1"))
                    .telephone("770000001")
                    .pays("Senegal")
                    .numeroPiece("SN123456")
                    .photo("alice.jpg")
                    .photoPiece("alice_piece.jpg")
                    .isActif(true)
                    .build();

            Utilisateur u2 = Utilisateur.builder()
                    .prenom("Bob")
                    .nom("Diop")
                    .email("bob.diop@email.com")
                    .motDePasse(passwordEncoder.encode("password2"))
                    .telephone("770000002")
                    .pays("Senegal")
                    .numeroPiece("SN654321")
                    .photo("bob.jpg")
                    .photoPiece("bob_piece.jpg")
                    .isActif(true)
                    .build();

            utilisateurRepository.saveAll(List.of(u1, u2));

        }

        if (compteRepository.findAll().isEmpty()) {
            Compte compte = Compte.builder()
                    .utilisateur(utilisateurRepository.findById(1L).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé")))
                    .solde(BigDecimal.valueOf(1000.00))
                    .active(true)
                    .dateCreation(LocalDateTime.now())
                    .build();

            Compte compte2 = Compte.builder()
                    .utilisateur(utilisateurRepository.findById(2L).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé")))
                    .solde(BigDecimal.valueOf(2000.00))
                    .active(true)
                    .dateCreation(LocalDateTime.now())
                    .build();

            compteRepository.saveAll(List.of(compte, compte2));
        }

        // Log the completion of data initialization
        log.info("Données initialisées avec succès.");

    }
}
