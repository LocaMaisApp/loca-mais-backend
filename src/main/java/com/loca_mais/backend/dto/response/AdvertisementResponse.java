package com.loca_mais.backend.dto.response;

import com.loca_mais.backend.model.PropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AdvertisementResponse
{
    private Integer id;
    private String description;
    private Double condominiumValue;
    private Double value;
    private Double iptuValue;
    private PropertyEntity property;
    private List<String> images;
}
