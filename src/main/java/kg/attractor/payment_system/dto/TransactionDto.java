package kg.attractor.payment_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
    private String status;
    private String transactionType;
    private Timestamp createdAt;
    private Timestamp approvedAt;
}
