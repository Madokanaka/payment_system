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
public class BalanceUpdateRequestDto {

    @Size(min = 20, max = 20, message = "Account number must be exactly 20 digits long.")
    @Pattern(regexp = "^[0-9]+$", message = "Account number must contain only digits.")
    private String accountNumber;

    @NotNull(message = "Amount cannot be null.")
    @MinTwoDecimalPlaces()
    private BigDecimal amount;


}
