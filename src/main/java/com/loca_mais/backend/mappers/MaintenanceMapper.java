package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.create.CreateMaintenanceDTO;
import com.loca_mais.backend.model.MaintenanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MaintenanceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    MaintenanceEntity creationDtoToEntity(CreateMaintenanceDTO dto);


}
