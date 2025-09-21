package sn.unchk.emoney_transfer.features.utilisateur.auth;

import lombok.Builder;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurResponseDto;

@Builder
public record AuthentificationResponse(
        String token, String typeToken,
        UtilisateurResponseDto utilisateur
) {
}
