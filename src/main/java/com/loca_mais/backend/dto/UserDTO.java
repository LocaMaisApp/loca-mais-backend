package com.loca_mais.backend.dto;

import java.util.Date;

public record UserDTO(
        int id,
        String name,
        String lastName,
        String cpf,
        String phone,
        String email,
        Date createdAt,
        Date updatedAt,
        boolean active
) {
}
