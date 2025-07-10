package com.loca_mais.backend.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateContractDTO(
        @Min(1)
        @Max(31)
        int payment_day,

        @Positive
        float monthly_value,

        @Min(1)
        int duration,

        @Positive
        float deposit,

        @NotNull
        int property_id,

        @NotNull
        int Tenant_User_id
) {
}
