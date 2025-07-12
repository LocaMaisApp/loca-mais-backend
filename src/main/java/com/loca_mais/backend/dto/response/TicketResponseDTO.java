package com.loca_mais.backend.dto.response;

import com.loca_mais.backend.enums.TickerStatus;

import java.util.Date;

public record TicketResponseDTO(
        int id,
        boolean urgent,
        String description,
        TickerStatus status,
        Date created_at,
        Date updated_at,
        int property_id,
        int tenant_id
) {
}
