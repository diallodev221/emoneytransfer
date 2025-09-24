package sn.unchk.emoney_transfer.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.unchk.emoney_transfer.features.compte.CompteRepository;
import sn.unchk.emoney_transfer.features.compte.CompteService;
import sn.unchk.emoney_transfer.features.transaction.TransactionRepository;
import sn.unchk.emoney_transfer.features.transaction.TransactionService;
import sn.unchk.emoney_transfer.features.utilisateur.UtilisateurRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CompteRepository compteRepository;
    private final TransactionRepository transactionRepository;
    private final UtilisateurRepository utilisateurRepository;


    public Map<String, Object> getStatistics() {
        Map<String, Object> params = new HashMap<>();

        long utilisateurCount = utilisateurRepository.count();
        long transactionCount = transactionRepository.count();
        long totalFraisTransaction = transactionRepository.getTotalFraisTransaction();
        long totalSolde = compteRepository.totalSolde();


        params.put("totalUsers",  utilisateurCount);
        params.put("totalTransactions", transactionCount);
        params.put("totalBalance", totalSolde);
        params.put("totalFees",totalFraisTransaction);

        return params;
    }
}
