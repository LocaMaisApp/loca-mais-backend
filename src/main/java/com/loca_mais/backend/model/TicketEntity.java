package com.loca_mais.backend.model;

import com.loca_mais.backend.core.AbstractModel;
import com.loca_mais.backend.enums.TickerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity {
    public int id;
    private Date createdAt=new Date();
    private Date updatedAt=new Date();
    private boolean urgent;
    private String description;
    private TickerStatus status;
    private int property_id;
    private int tenant_id;
}
