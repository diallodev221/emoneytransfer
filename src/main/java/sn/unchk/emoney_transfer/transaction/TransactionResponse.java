package sn.unchk.emoney_transfer.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(String source, String destinataire, BigDecimal montant, LocalDateTime date) {}
