package kg.attractor.payment_system.exception;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends NoSuchElementException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
