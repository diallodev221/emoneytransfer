package sn.unchk.emoney_transfer.features.utilisateur;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import sn.unchk.emoney_transfer.features.compte.Compte;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;
import sn.unchk.emoney_transfer.features.utilisateur.profile.Profile;
import sn.unchk.emoney_transfer.features.utilisateur.profile.ProfileRepository;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepo;
    private final CompteRepository compteRepo;
    private final ProfileRepository profileRepo;
    private final UtilisateurMapper mapper;


    @Transactional
    public UtilisateurResponseDto enregistrer(RegisterRequest request) {
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

        return mapper.toDto(savedUser);
    }


    public List<UtilisateurResponseDto> getAll() {
        return utilisateurRepo.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public UtilisateurResponseDto update(@PathVariable Long id, @RequestBody RegisterRequest request) {
        Utilisateur utilisateur = utilisateurRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Utilisateur not found"));

        utilisateur.setPrenom(request.prenom());
        utilisateur.setNom(request.nom());
        utilisateur.setEmail(request.email());
        utilisateur.setTelephone(request.telephone());
        utilisateur.setPays(request.pays());

        return mapper.toDto(utilisateurRepo.save(utilisateur));

    }

    public Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName();
        return utilisateurRepo.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }
}
