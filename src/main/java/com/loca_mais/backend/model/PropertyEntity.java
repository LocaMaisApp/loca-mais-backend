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
public class PropertyEntity extends AbstractModel {
    private String name;
    private String street;
    private String city;
    private String state;
    private String complement;
    private Integer number;
    private Integer size;
    private Integer bathroomQuantity;
    private Integer suites;
    private Integer car_space;
    private Integer roomQuantity;
    private Integer landlord_id;
}
