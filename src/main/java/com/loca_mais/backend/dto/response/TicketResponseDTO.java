package com.loca_mais.backend.dto.response;

import com.loca_mais.backend.enums.TickerStatus;

import java.util.Date;

public record TicketResponseDTO(
        int id,
        boolean urgent,
        String description,
        TickerStatus status,
        Date createdAt,
        Date updatedAt,
        int property_id,
        int tenant_id,
        boolean active
) {
}
