package com.loca_mais.backend.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketDTO(
        @NotNull
        boolean urgent,

        @NotBlank
        String description,

        @NotNull
        int property_id
) {
}
