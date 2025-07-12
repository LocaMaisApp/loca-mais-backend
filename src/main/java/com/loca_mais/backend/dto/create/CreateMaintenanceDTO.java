package com.loca_mais.backend.dto.create;

import java.math.BigDecimal;
import java.util.Date;

public record CreateMaintenanceDTO(
        BigDecimal total_value,
        Date finished_at,
        int ticket_id
) {
}
