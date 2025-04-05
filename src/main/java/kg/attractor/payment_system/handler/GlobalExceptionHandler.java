package kg.attractor.payment_system.handler;

import kg.attractor.payment_system.exception.BadRequestException;
import kg.attractor.payment_system.exception.DatabaseOperationException;
import kg.attractor.payment_system.exception.RecordAlreadyExistsException;
import kg.attractor.payment_system.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorService errorService;

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNotFoundException(NoSuchElementException ex) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage()).build();
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<String> handleDatabaseOperationException(DatabaseOperationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> validationHandler(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(errorService.makeResponse(ex.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<String> handleRecordAlreadyExistsException(RecordAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
