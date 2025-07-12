package com.loca_mais.backend.model;

import com.loca_mais.backend.core.AbstractModel;
import com.loca_mais.backend.enums.TickerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TicketEntity extends AbstractModel {
    private boolean urgent;
    private String description;
    private TickerStatus status;
    private int property_id;
    private int tenant_id;
}
