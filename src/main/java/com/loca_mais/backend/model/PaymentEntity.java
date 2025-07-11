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
public class PaymentEntity extends AbstractModel {

        private Double value;
        private Double tax;
        private Integer contractId;

}
