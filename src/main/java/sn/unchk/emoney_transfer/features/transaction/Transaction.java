package sn.unchk.emoney_transfer.features.transaction;

import jakarta.persistence.*;
import lombok.*;
import sn.unchk.emoney_transfer.enums.StatusTransaction;
import sn.unchk.emoney_transfer.enums.TypeTransaction;
import sn.unchk.emoney_transfer.features.compte.Compte;

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
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SOURCE_ID", referencedColumnName = "id")
    private Compte source;

    @ManyToOne
    @JoinColumn(name = "DESTINATAIRE_ID", referencedColumnName = "id")
    private Compte destinataire;

    @Column(name = "MONTANT")
    private BigDecimal montant;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "DATE_OPERATION")
    private LocalDateTime date;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FRAIS")
    private Double frais;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private TypeTransaction typeTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private StatusTransaction status;


}