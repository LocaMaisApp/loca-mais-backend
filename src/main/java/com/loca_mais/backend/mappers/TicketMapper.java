package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.create.CreateTicketDTO;
import com.loca_mais.backend.dto.response.TicketResponseDTO;
import com.loca_mais.backend.model.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant_id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    TicketEntity creationDtoToEntity(CreateTicketDTO dto);

    TicketResponseDTO entityToResponseDto(TicketEntity entity);

    List<TicketResponseDTO> entityListToResponseDtoList(List<TicketEntity> entities);

}
