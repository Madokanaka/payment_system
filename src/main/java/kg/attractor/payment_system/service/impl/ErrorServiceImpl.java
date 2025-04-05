package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.handler.ErrorResponseBody;
import kg.attractor.payment_system.service.ErrorService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody makeResponse(Exception ex) {
        String msg = ex.getMessage();
        return ErrorResponseBody.builder()
                .title(msg)
                .response(Map.of("errors", List.of(msg)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(BindingResult bindingResult) {
        Map<String, List<String>> reasons = new HashMap<>();
        bindingResult.getFieldErrors().stream()
                .filter(err -> err.getDefaultMessage() != null)
                .forEach(err -> {
                    List<String> errors = new ArrayList<>();
                    errors.add(err.getDefaultMessage());
                    if (!reasons.containsKey(err.getField())) {
                        reasons.put(err.getField(), errors);
                    }
                });
        return ErrorResponseBody.builder()
                .title("Validation Errors")
                .response(reasons)
                .build();
    }
}
