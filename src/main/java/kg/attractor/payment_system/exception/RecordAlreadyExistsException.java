package kg.attractor.payment_system.exception;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
