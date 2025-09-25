package sn.unchk.emoney_transfer.features.utilisateur;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }


    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDto>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDto> update(@PathVariable Long id, @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(utilisateurService.update(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeUserStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        utilisateurService.ChangeUserStatus(id, Boolean.parseBoolean(status));
        return ResponseEntity.ok().build();
    }
}