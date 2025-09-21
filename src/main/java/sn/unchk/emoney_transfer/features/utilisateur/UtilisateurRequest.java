package sn.unchk.emoney_transfer.features.utilisateur;

public class UtilisateurRequest {
    private String prenom;
    private String nom;
    private String telephone;
    private String email;
    private String pays;
    private String photo;
    private String numeroPiece;
    private String photoPiece;

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getPays() {
        return pays;
    }

    public String getPhoto() {
        return photo;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public String getPhotoPiece() {
        return photoPiece;
    }
}
