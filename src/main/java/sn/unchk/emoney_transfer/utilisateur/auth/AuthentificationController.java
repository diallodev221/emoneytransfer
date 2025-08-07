package sn.unchk.emoney_transfer.utilisateur.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthentificationController {

    private final AuthentificationService authentificationService;
}
