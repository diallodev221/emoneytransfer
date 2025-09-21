package sn.unchk.emoney_transfer.features.utilisateur;

import org.springframework.stereotype.Service;

@Service
public class UtilisateurMapper {

    public UtilisateurResponseDto toDto(Utilisateur utilisateur) {
        return UtilisateurResponseDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .telephone(utilisateur.getTelephone())
                .pays(utilisateur.getPays())
                .numeroPiece(utilisateur.getNumeroPiece())
                .photo(utilisateur.getPhoto())
                .photoPiece(utilisateur.getPhotoPiece())
                .build();
    }
}
