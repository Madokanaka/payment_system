package kg.attractor.payment_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRollback {
    private Long id;
    private Long transactionId;
    private Timestamp rollbackedAt;
    private BigDecimal amount;
}
