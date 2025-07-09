package com.loca_mais.backend.model;

import com.loca_mais.backend.core.AbstractModel;
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
public class ContractEntity extends AbstractModel {
    private int payment_day;
    //Mudar depois o tipo float, vamos ver qual estratégia iremos utilizar
    private float monthly_value;
    private int duration;
    //Mudar depois o tipo float, vamos ver qual estratégia iremos utilizar
    private float deposit;
    private int property_id;
    private int Tenant_User_id;
}
