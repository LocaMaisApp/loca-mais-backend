package com.loca_mais.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateContractResponseDTO(
        int id,
        LocalDateTime created_at,
        int payment_day,
        BigDecimal monthly_value,
        int duration,
        BigDecimal deposit
) {
}
