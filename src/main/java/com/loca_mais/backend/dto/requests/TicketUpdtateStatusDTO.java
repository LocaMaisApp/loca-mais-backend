package com.loca_mais.backend.dto.requests;

import com.loca_mais.backend.enums.TickerStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TicketUpdtateStatusDTO(
        @NotNull
        TickerStatus status,
        BigDecimal total_value
) {
}
