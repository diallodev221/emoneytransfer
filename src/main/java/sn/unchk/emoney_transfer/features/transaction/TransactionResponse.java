package sn.unchk.emoney_transfer.features.transaction;

import sn.unchk.emoney_transfer.features.compte.CompteDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id, CompteDto source, CompteDto destinataire,
        BigDecimal montant, BigDecimal totalAmount, LocalDateTime date,
        String description, String type, String status) {}
