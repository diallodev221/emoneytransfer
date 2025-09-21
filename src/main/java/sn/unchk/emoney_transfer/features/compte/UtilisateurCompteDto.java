package sn.unchk.emoney_transfer.features.compte;

import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;

import java.io.Serializable;

/**
 * DTO for {@link Utilisateur}
 */
public record UtilisateurCompteDto(Long id, String prenom, String nom) implements Serializable {
  }