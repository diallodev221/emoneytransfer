package sn.unchk.emoney_transfer.transaction;

import java.math.BigDecimal;

public record TransactionRequest(Long destinataireId, BigDecimal montant) {}