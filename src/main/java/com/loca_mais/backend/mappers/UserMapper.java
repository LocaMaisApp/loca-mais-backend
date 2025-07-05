package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.enums.UserType;
import com.loca_mais.backend.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    public UserResponseDTO toUserResponseDTO(UserEntity userEntity, UserType type);
}
