package sn.unchk.emoney_transfer.features.utilisateur.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.features.compte.Compte;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;
import sn.unchk.emoney_transfer.security.jwt.JwtService;
import sn.unchk.emoney_transfer.features.utilisateur.RegisterRequest;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurMapper;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurRepository;
import sn.unchk.emoney_transfer.features.utilisateur.profile.Profile;
import sn.unchk.emoney_transfer.features.utilisateur.profile.ProfileRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthentificationService {

    private final UtilisateurRepository utilisateurRepository;
    private final ProfileRepository profileRepository;
    private final CompteRepository compteRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UtilisateurMapper utilisateurMapper;


    @Transactional
    public Utilisateur signup(RegisterRequest request)  {
        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email déjà utilisé");
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


    public AuthentificationResponse login(AuthentificationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));


        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'email: " + request.email()));

//        if (!utilisateur.getMotDePasse().equals(request.password())) {
//            throw new IllegalArgumentException("Mot de passe incorrect");
//        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);

        return AuthentificationResponse.builder()
                .token(jwt)
                .typeToken("Bearer")
                .utilisateur(utilisateurMapper.toDto(utilisateur))
                .build();
    }


}
