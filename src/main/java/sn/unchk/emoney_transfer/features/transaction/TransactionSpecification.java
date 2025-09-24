package sn.unchk.emoney_transfer.features.transaction;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sn.unchk.emoney_transfer.enums.StatusTransaction;
import sn.unchk.emoney_transfer.enums.TypeTransaction;

import java.util.ArrayList;
import java.util.List;

public final class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> filter(TypeTransaction type, StatusTransaction status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (type != null) {
                predicates.add(cb.equal(root.get("typeTransaction"), type));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
