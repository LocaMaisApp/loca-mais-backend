package com.loca_mais.backend.dto.create;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateContractDTO(
        @Min(1)
        @Max(31)
        int payment_day,

        @Positive
        BigDecimal monthly_value,

        @Min(1)
        int duration,

        @Positive
        BigDecimal deposit,

        @NotNull
        int property_id,

        @NotBlank
        @Email
        String tenantEmail
) {
}
