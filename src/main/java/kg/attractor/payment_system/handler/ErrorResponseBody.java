package kg.attractor.payment_system.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseBody {
    private String title;
    private Map<String, List<String>> response;
}
