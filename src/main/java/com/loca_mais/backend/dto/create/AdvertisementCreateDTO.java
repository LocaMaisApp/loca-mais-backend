package com.loca_mais.backend.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record AdvertisementCreateDTO(

    @NotBlank
    String description,
    @NotNull
    Double condominiumValue,
    @NotNull
    Double value,
    @NotNull
    Double iptuValue,
    @NotNull
    Integer propertyId



) {
}
