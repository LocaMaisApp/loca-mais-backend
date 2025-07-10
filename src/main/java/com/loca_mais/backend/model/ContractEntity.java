package com.loca_mais.backend.model;

import com.loca_mais.backend.core.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ContractEntity extends AbstractModel {
    private int payment_day;
    private BigDecimal monthly_value;
    private int duration;
    private BigDecimal deposit;
    private int property_id;
    private int tenant_id;
}
