package kg.attractor.payment_system.exception;

import java.util.NoSuchElementException;

public class TransactionNotFoundException extends NoSuchElementException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
