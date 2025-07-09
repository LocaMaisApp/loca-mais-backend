package com.loca_mais.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvertisementImagesEntity {
    private String url;
    private Integer advertisementId;
}
