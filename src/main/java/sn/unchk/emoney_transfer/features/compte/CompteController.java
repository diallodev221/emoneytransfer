package sn.unchk.emoney_transfer.features.compte;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
public class CompteController {

    private final CompteService compteService;


    @PostMapping("/creer")
    public ResponseEntity<CompteDto> creerCompte(
            @RequestParam("user") Long utilisateurId,
            @RequestParam(required = false) BigDecimal soldeInitial
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compteService.creerCompte(utilisateurId, soldeInitial));
    }

    @PostMapping("/{id}/crediter")
    public ResponseEntity<CompteDto> crediter(
            @PathVariable Long id,
            @RequestParam BigDecimal montant
    ) {
        return ResponseEntity.ok(compteService.crediter(id, montant));
    }

    @PostMapping("/{id}/debiter")
    public ResponseEntity<CompteDto> debiter(
            @PathVariable Long id,
            @RequestParam BigDecimal montant
    ) throws BadRequestException {
        return ResponseEntity.ok(compteService.debiter(id, montant));
    }

    @GetMapping("/{id}/solde")
    public ResponseEntity<Map<String, BigDecimal>> consulterSolde(@PathVariable Long id) {
        return ResponseEntity.ok(compteService.consulterSolde(id));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<CompteDto> desactiverCompte(@PathVariable Long id) {
        return ResponseEntity.ok(compteService.desactiverCompte(id));
    }

    @PatchMapping("/{id}/reactiver")
    public ResponseEntity<CompteDto> reactiverCompte(@PathVariable Long id) {
        return ResponseEntity.ok(compteService.reactiverCompte(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCompte(@PathVariable Long id) {
        compteService.supprimerCompte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<CompteDto> getComptesParUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(compteService.getComptesParUtilisateur(utilisateurId));
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<CompteDto>> getComptesActifs() {
        return ResponseEntity.ok(compteService.getComptesActifs());
    }

    @GetMapping
    public ResponseEntity<List<CompteDto>> getComptes() {
        return ResponseEntity.ok(compteService.getComptes());
    }

    @GetMapping("/envoyees")
    public ResponseEntity<List<CompteDto>> getComptesEnvoyees() {
        return ResponseEntity.ok(compteService.getComptesEnvoyees());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompteDto> getCompte(@PathVariable Long id) {
        return ResponseEntity.ok(compteService.getCompteById(id));
    }
}
