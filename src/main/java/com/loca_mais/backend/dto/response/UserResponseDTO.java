package com.loca_mais.backend.dto.response;

import com.loca_mais.backend.enums.UserType;
import lombok.Setter;

import java.util.Date;

public record UserResponseDTO(
        int id,
        String name,
        String lastName,
        String cpf,
        String phone,
        String email,
        Date createdAt,
        Date updatedAt,
        boolean active,
        UserType type
) {
}
