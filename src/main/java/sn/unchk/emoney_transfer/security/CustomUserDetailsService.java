package sn.unchk.emoney_transfer.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurRepository;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UtilisateurRepository userRepository;

    public CustomUserDetailsService(UtilisateurRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable ou compte inactif"));


        // Collect all authorities (roles and permissions)
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Add roles with ROLE_ prefix as required by Spring Security
        if (user.getProfile() != null && user.getProfile().getName() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getProfile().getName()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getMotDePasse(),
                user.isActif(),
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities
        );
    }
}
