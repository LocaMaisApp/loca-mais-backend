package com.loca_mais.backend.dto.create;

public record PaymentCreateDTO(
        Double value,
        Double tax,
        Integer contractId
) {
}
