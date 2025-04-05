package kg.attractor.payment_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kg.attractor.payment_system.validation.MinTwoDecimalPlaces;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {

    @NotNull(message = "Sender account number is required.")
    @Size(min = 20, max = 20, message = "Sender account number must be exactly 20 characters long.")
    @Pattern(regexp = "^[0-9]+$", message = "Sender account number must contain only digits.")
    private String senderAccountNumber;

    @NotNull(message = "Receiver account number is required.")
    @Size(min = 20, max = 20, message = "Receiver account number must be exactly 20 characters long.")
    @Pattern(regexp = "^[0-9]+$", message = "Receiver account number must contain only digits.")
    private String receiverAccountNumber;

    @NotNull(message = "Amount is required.")
    @MinTwoDecimalPlaces()
    private BigDecimal amount;

}
