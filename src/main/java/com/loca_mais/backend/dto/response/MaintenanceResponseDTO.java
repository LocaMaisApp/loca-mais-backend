package com.loca_mais.backend.dto.response;

import com.loca_mais.backend.enums.TickerStatus;
import com.loca_mais.backend.model.PropertyEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record MaintenanceResponseDTO(
        int id,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        LocalDateTime finished_at,
        BigDecimal total_value,
        TicketDTO ticket
) {
    @Builder
    public record TicketDTO(
            int id,
            LocalDateTime created_at,
            LocalDateTime updated_at,
            boolean urgent,
            String description,
            TickerStatus status,
            PropertyEntity property,
            int tenant_id
    ) {}
}