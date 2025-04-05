package kg.attractor.payment_system.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MinTwoDecimalPlacesValidator implements ConstraintValidator<MinTwoDecimalPlaces, BigDecimal> {

    @Override
    public void initialize(MinTwoDecimalPlaces constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            return false;
        }

        BigDecimal roundedValue = value.setScale(2, RoundingMode.HALF_UP);

        return value.compareTo(roundedValue) == 0;
    }
}
