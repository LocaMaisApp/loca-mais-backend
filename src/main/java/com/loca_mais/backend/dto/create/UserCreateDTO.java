package com.loca_mais.backend.dto.create;

public record UserCreateDTO(
        String name,
        String lastName,
        String cpf,
        String phone,
        String email,
        String password
) {}
