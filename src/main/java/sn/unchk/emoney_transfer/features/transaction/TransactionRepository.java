package sn.unchk.emoney_transfer.features.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    @Query("SELECT tx FROM Transaction tx WHERE tx.source.id = ?1 OR tx.destinataire.id = ?1")
    List<Transaction> getAllTransactions(Long compteId);

    @Query("SELECT COALESCE(SUM(tx.frais), 0) FROM Transaction tx")
    long getTotalFraisTransaction();

    @Query("SELECT tx FROM Transaction tx WHERE tx.source.utilisateur.id = :userId OR  tx.destinataire.utilisateur.id = :userId ORDER BY tx.date DESC LIMIT 10")
    List<Transaction> getTenRecentsTransactions(Long userId);

    @Query("SELECT tx FROM Transaction tx WHERE (:type IS NULL OR tx.typeTransaction = :type) AND (:status IS NULL OR tx.status = :status) ORDER BY tx.date DESC")
    List<Transaction> getFilteredTransactions(@Param("type") String type, @Param("status") String status);
}