package com.loca_mais.backend.dto.requests;

public record AuthLoginDTO(
        String email,
        String password
) {
}
