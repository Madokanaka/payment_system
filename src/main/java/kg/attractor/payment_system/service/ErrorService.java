package kg.attractor.payment_system.service;

import kg.attractor.payment_system.handler.ErrorResponseBody;
import org.springframework.validation.BindingResult;

public interface ErrorService {
    ErrorResponseBody makeResponse(Exception ex);

    ErrorResponseBody makeResponse(BindingResult bindingResult);
}
