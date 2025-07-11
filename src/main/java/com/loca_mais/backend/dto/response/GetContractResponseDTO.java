package com.loca_mais.backend.dto.response;

import com.loca_mais.backend.model.PropertyEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetContractResponseDTO(
        int id,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        int payment_day,
        BigDecimal monthly_value,
        int duration,
        BigDecimal deposit,
        int tenant_id,
        boolean active,
        PropertyEntity propertyEntity
) {
}
