package kg.attractor.payment_system.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MinTwoDecimalPlacesValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinTwoDecimalPlaces {
    String message() default "Amount must be greater than or equal to 0.01 and have exactly two decimal places.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
