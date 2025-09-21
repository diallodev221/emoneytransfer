package sn.unchk.emoney_transfer.features.utilisateur;

import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link Utilisateur}
 */
@Builder
public record UtilisateurRequestDto(String prenom, String nom, String email, String telephone, String pays,
                                    String numeroPiece, String photo, String photoPiece, Long profileId) implements Serializable {
}