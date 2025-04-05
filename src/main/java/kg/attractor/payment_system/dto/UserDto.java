package kg.attractor.payment_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Pattern(regexp = "996\\s\\(\\d{3}\\)\\s\\d{2}-\\d{2}-\\d{2}", message = "Phone number must match pattern: 996 (XXX) XX-XX-XX")
    private String phoneNumber;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters long")
    private String password;
}
