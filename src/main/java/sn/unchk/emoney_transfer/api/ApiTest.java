package sn.unchk.emoney_transfer.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class ApiTest {

    @GetMapping
    public Map<String, String> hello() {
        return Map.of("message", "Hello from Spring Boot!") ;
    }
}
