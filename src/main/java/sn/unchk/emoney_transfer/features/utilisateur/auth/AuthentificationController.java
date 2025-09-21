package sn.unchk.emoney_transfer.features.utilisateur.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthentificationController {

    private final AuthentificationService authentificationService;


    @PostMapping("/login")
    public ResponseEntity<AuthentificationResponse> login(@RequestBody AuthentificationRequest request) {
        return ResponseEntity.ok(authentificationService.login(request));
    }
}
