package kg.attractor.payment_system.exception;

import java.util.NoSuchElementException;

public class AccountNotFoundException extends NoSuchElementException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
