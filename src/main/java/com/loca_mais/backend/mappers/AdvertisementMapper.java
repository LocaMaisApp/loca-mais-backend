package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.create.AdvertisementCreateDTO;
import com.loca_mais.backend.dto.response.AdvertisementResponse;
import com.loca_mais.backend.model.AdvertisementEntity;
import com.loca_mais.backend.model.PropertyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdvertisementMapper {


    AdvertisementEntity toEntity(AdvertisementCreateDTO advertisementCreateDTO);



}
