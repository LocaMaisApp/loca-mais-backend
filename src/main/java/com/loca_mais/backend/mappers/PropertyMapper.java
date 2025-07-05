package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.create.PropertyRegistrationDTO;
import com.loca_mais.backend.model.PropertyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PropertyMapper {

    PropertyEntity  toEntity(PropertyRegistrationDTO propertyRegistrationDTO);
}
