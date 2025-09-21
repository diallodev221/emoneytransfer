package sn.unchk.emoney_transfer.features.compte;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comptes")
public class Compte {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "SOLDE", nullable = false)
    private BigDecimal solde = BigDecimal.ZERO;

    @Column(name = "NUMERO_COMPTE", unique = true, nullable = false)
    private String numeroCompte;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    private Utilisateur utilisateur;
}
