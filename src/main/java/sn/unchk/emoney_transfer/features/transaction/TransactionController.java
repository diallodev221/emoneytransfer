package sn.unchk.emoney_transfer.features.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{sourceId}")
    public ResponseEntity<TransactionResponse> envoyer(@PathVariable Long sourceId, @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.effectuerTransaction(sourceId, request));
    }
}