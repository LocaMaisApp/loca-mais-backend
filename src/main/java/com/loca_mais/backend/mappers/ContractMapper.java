package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.response.CreateContractResponseDTO;
import com.loca_mais.backend.model.ContractEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {

    @Mapping(source = "createdAt", target = "created_at")
    CreateContractResponseDTO toCreateContractResponseDTO(ContractEntity contractEntity);

}
