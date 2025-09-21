package sn.unchk.emoney_transfer.features.utilisateur;

import lombok.Builder;
import sn.unchk.emoney_transfer.features.utilisateur.profile.Profile;

import java.io.Serializable;

/**
 * DTO for {@link Utilisateur}
 */
@Builder
public record UtilisateurResponseDto(Long id, String prenom, String nom, String email, String telephone, String pays,
                                     String numeroPiece, String photo, String photoPiece,
                                     Profile profile) implements Serializable {
}