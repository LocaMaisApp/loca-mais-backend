package com.loca_mais.backend.dto.create;

import com.loca_mais.backend.enums.UserType;

public record AuthRegisterDTO(
        String name,
        String lastName,
        String cpf,
        String phone,
        String email,
        String password,
        UserType type
) {}
