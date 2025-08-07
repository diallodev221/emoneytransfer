package sn.unchk.emoney_transfer.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import sn.unchk.emoney_transfer.utilisateur.profile.Profile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prenom;
    private String nom;
    private String email;
    private String motDePasse;
    private String telephone;
    private String pays;
    private String numeroPiece;
    private String photo;
    private String photoPiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;
}
