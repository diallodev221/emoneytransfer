package sn.unchk.emoney_transfer.compte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    List<Compte> findByUtilisateurId(Long utilisateurId);

    List<Compte> findByActiveTrue();
}