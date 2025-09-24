package sn.unchk.emoney_transfer.features.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sn.unchk.emoney_transfer.enums.StatusTransaction;
import sn.unchk.emoney_transfer.enums.TypeTransaction;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/{sourceId}")
    public void envoyer(@PathVariable Long sourceId, @RequestBody TransactionRequest request) {
        transactionService.effectuerTransaction(sourceId, request);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/10-recent")
    public List<TransactionResponse> getTenRecentTransactions() {
        return transactionService.getTenRecentTransactions();
    }

    @GetMapping("/filter")
    public List<TransactionResponse> getFilteredTransactions(
            @RequestParam(value = "type", required = false) TypeTransaction type,
            @RequestParam(value = "status", required = false) StatusTransaction status) {
        return transactionService.getFilteredTransactions(type, status);
    }

    @GetMapping("/current-user")
    public List<TransactionResponse> getAllTransactionsCurrentUser() {
        return transactionService.getAllTransactionsCurrentUser();
    }
}