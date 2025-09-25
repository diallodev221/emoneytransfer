package sn.unchk.emoney_transfer.features.transaction;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sn.unchk.emoney_transfer.enums.StatusTransaction;
import sn.unchk.emoney_transfer.enums.TypeTransaction;
import sn.unchk.emoney_transfer.features.utilisateur.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> filter(TypeTransaction type, StatusTransaction status, Utilisateur currentUser) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getId())) {
                predicates.add(cb.equal(root.get("source").get("utilisateur").get("id"), currentUser.getId()));
            }
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
