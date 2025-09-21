package sn.unchk.emoney_transfer.features.compte;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByUtilisateurId(Long utilisateurId);

    @EntityGraph(attributePaths = {"utilisateur"})
    @Query("SELECT c FROM Compte c WHERE c.id != :id AND c.active IS TRUE")
    List<Compte> findActiveComptes(@Param("id") Long userId);

    @EntityGraph(attributePaths = {"utilisateur"})
    @Query("SELECT c FROM Compte c WHERE c.active IS TRUE")
    List<Compte> findAllComptes();

    @EntityGraph(attributePaths = {"utilisateur"})
    @Query("SELECT c FROM Compte c WHERE c.active IS TRUE AND c.id != :compteId")
    List<Compte> findAllComptesPourEnvoie(Long compteId);
}