package sn.unchk.emoney_transfer.features.compte;

import org.springframework.stereotype.Service;

@Service
public class CompteMapper {

    public CompteDto toDto(Compte compte) {
        return new CompteDto(compte.getId(), new UtilisateurCompteDto(compte.getUtilisateur().getId(), compte.getUtilisateur().getPrenom(), compte.getUtilisateur().getNom()), compte.getSolde(), compte.isActive(), compte.getDateCreation(), compte.getNumeroCompte());
    }
}
