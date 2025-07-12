package com.loca_mais.backend.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaintenanceEntity {
    private int id;
    private Date created_at = new Date();
    private Date updated_at = new Date();
    private Date finished_at = new Date();
    private BigDecimal total_value;
    private int ticket_id;
}