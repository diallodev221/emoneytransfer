package sn.unchk.emoney_transfer.compte;

import java.io.Serializable;

/**
 * DTO for {@link sn.unchk.emoney_transfer.utilisateur.Utilisateur}
 */
public record UtilisateurCompteDto(Long id, String prenom, String nom) implements Serializable {
  }