package sn.unchk.emoney_transfer.features.compte;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link Compte}
 */
public record CompteDto(Long id, UtilisateurCompteDto utilisateur, BigDecimal solde, boolean active, LocalDateTime dateCreation,
                        String numeroCompte) implements Serializable {
  }