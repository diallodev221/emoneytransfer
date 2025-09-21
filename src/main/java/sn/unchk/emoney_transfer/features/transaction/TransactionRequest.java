package sn.unchk.emoney_transfer.features.transaction;

import java.math.BigDecimal;

public record TransactionRequest(Long destinataireId, String description, BigDecimal montant) {}