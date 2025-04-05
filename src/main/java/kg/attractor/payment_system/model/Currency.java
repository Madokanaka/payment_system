package kg.attractor.payment_system.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Currency {
    private Long id;
    private String currencyName;
}

