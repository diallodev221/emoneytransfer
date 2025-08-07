package sn.unchk.emoney_transfer.utilisateur;

public record RegisterRequest(String prenom, String nom, String email, String motDePasse, String pays, String telephone) {}
