package sn.unchk.emoney_transfer.features.utilisateur.auth;

public record AuthentificationRequest(
        String email, String password
) {
}
