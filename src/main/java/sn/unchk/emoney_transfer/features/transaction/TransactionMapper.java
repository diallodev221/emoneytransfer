package sn.unchk.emoney_transfer.features.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.unchk.emoney_transfer.features.compte.CompteMapper;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TransactionMapper {

    private final CompteMapper mapper;

    public TransactionResponse toResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getSource() != null ? mapper.toDto(tx.getSource()) : null,
                tx.getDestinataire() != null ? mapper.toDto(tx.getDestinataire()) : null,
                tx.getMontant(),
                tx.getTotalAmount(),
                tx.getDate(),
                tx.getDescription(),
                Objects.nonNull(tx.getTypeTransaction()) ? tx.getTypeTransaction().name() : "",
                Objects.nonNull(tx.getStatus()) ? tx.getStatus().name() : ""
        );
    }
}
