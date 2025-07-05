package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.PropertyDAO;
import com.loca_mais.backend.dto.create.PropertyRegistrationDTO;
import com.loca_mais.backend.mappers.PropertyMapper;
import com.loca_mais.backend.model.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PropertyService {

    private final PropertyDAO propertyDAO;

    private final PropertyMapper propertyMapper;

    private final LandlordService landlordService;

    public List<PropertyEntity> findAll(){
        return propertyDAO.findAll();
    }

    public void create(PropertyRegistrationDTO propertyRegistrationDTO){
        PropertyEntity propertyEntity=propertyMapper.toEntity(propertyRegistrationDTO);
        landlordService.findById(propertyEntity.getLandlord_id());
        propertyDAO.save(propertyEntity);
    }
}
