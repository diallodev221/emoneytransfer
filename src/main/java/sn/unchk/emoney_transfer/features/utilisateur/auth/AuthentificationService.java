package sn.unchk.emoney_transfer.features.utilisateur.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.features.compte.Compte;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;
import sn.unchk.emoney_transfer.features.utilisateur.*;
import sn.unchk.emoney_transfer.security.jwt.JwtService;
import sn.unchk.emoney_transfer.features.utilisateur.profile.Profile;
import sn.unchk.emoney_transfer.features.utilisateur.profile.ProfileRepository;
import sn.unchk.emoney_transfer.utils.CodeGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UtilisateurResponseDto register(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setPrenom(request.prenom());
        user.setNom(request.nom());
        user.setEmail(request.email());
        user.setMotDePasse(passwordEncoder.encode(request.motDePasse()));
        user.setTelephone(request.telephone());
        user.setPays(request.pays());
        user.setActif(true);
        user.setDateInscription(LocalDate.now());
        // Générer un numéro de pièce d'identité unique
        user.setNumeroPiece(request.numeroPiece());

        Profile profile = profileRepository.findByName("UTILISATEUR")
                .orElseThrow(() -> new EntityNotFoundException("Profile 'UTILISATEUR' not found"));

        user.setProfile(profile);

        Utilisateur savedUser = utilisateurRepository.save(user);
        Compte compte = Compte.builder()
                .numeroCompte(CodeGenerator.generateBankAccountNumber(16))
                .utilisateur(savedUser)
                .solde(BigDecimal.ZERO)
                .active(true)
                .dateCreation(LocalDateTime.now())
                .build();

        compteRepository.save(compte);

        return utilisateurMapper.toDto(savedUser);
    }


    public AuthentificationResponse login(AuthentificationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'email: " + request.email()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);

        return AuthentificationResponse.builder()
                .token(jwt)
                .typeToken("Bearer")
                .utilisateur(utilisateurMapper.toDto(utilisateur))
                .build();
    }
}
