package com.loca_mais.backend.model;

import com.loca_mais.backend.core.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdvertisementEntity extends AbstractModel {

    private String description;
    private Double condominiumValue;
    private Double value;
    private Double iptuValue;
    private Integer propertyId;


}
