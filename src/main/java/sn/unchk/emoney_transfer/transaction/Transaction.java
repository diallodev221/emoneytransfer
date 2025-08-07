package sn.unchk.emoney_transfer.transaction;

import jakarta.persistence.*;
import lombok.*;
import sn.unchk.emoney_transfer.compte.Compte;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private Compte source;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", referencedColumnName = "id")
    private Compte destinataire;

    private BigDecimal montant;
    private LocalDateTime date;
}