package kg.attractor.payment_system.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }
}
