package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.create.CreateTicketDTO;
import com.loca_mais.backend.dto.response.TicketResponseDTO;
import com.loca_mais.backend.enums.TickerStatus;
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
    TicketEntity creationDtoToEntity(CreateTicketDTO dto);

    @Mapping(target = "active", expression = "java(isActive(entity.getStatus()))")
    TicketResponseDTO entityToResponseDto(TicketEntity entity);

    List<TicketResponseDTO> entityListToResponseDtoList(List<TicketEntity> entities);

    default boolean isActive(TickerStatus status) {
        return status == TickerStatus.PENDENT || status == TickerStatus.PROGRESS;
    }
}
