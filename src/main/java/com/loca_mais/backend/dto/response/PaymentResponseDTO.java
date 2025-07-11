package com.loca_mais.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private Integer id;
    private BigDecimal value;
    private BigDecimal tax;
    private LocalDateTime createdAt;

}
