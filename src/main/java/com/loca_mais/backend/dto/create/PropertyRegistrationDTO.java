package com.loca_mais.backend.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRegistrationDTO {

    @NotBlank(message = "O nome da propriedade é obrigatório.")
    private String name;

    @NotBlank(message = "A rua é obrigatória.")
    private String street;

    @NotBlank(message = "A cidade é obrigatória.")
    private String city;

    @NotBlank(message = "O estado é obrigatório.")
    private String state;

    private String complement;

    @NotNull(message = "O número é obrigatório.")
    @Min(value = 1, message = "O número deve ser maior que 0.")
    private Integer number;

    @NotNull(message = "O tamanho da propriedade é obrigatório.")
    @Min(value = 1, message = "O tamanho deve ser maior que 0.")
    private Integer size;

    @NotNull(message = "A quantidade de banheiros é obrigatória.")
    @Min(value = 0, message = "A quantidade de banheiros não pode ser negativa.")
    private Integer bathroomQuantity;

    @NotNull(message = "A quantidade de suítes é obrigatória.")
    @Min(value = 0, message = "A quantidade de suítes não pode ser negativa.")
    private Integer suites;

    @NotNull(message = "A quantidade de vagas de garagem é obrigatória.")
    @Min(value = 0, message = "A quantidade de vagas de garagem não pode ser negativa.")
    private Integer car_space;

    @NotNull(message = "A quantidade de quartos é obrigatória.")
    @Min(value = 0, message = "A quantidade de quartos não pode ser negativa.")
    private Integer roomQuantity;

    @NotNull(message = "O ID do proprietário é obrigatório.")
    @Min(value = 1, message = "O ID do proprietário deve ser maior que 0.")
    private Integer landlord_id;
}