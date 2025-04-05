package kg.attractor.payment_system.exception;

import java.util.NoSuchElementException;

public class CurrencyNotFoundException extends NoSuchElementException {
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
