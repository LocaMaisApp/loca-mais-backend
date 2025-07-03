package com.loca_mais.backend.dto.response;

public record AuthLoginResponseDTO(
        String accessToken,
        UserResponseDTO user

) {
}
