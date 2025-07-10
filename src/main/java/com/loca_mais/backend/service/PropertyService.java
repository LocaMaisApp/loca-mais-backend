package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.PropertyDAO;
import com.loca_mais.backend.dto.create.PropertyRegistrationDTO;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.mappers.PropertyMapper;
import com.loca_mais.backend.model.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PropertyService {

    private final PropertyDAO propertyDAO;

    private final PropertyMapper propertyMapper;

    private final LandlordService landlordService;

    public List<PropertyEntity> findAll(){
        return propertyDAO.findAll();
    }

    public PropertyEntity create(PropertyRegistrationDTO propertyRegistrationDTO){
        PropertyEntity propertyEntity=propertyMapper.toEntity(propertyRegistrationDTO);
        landlordService.findById(propertyEntity.getLandlord_id());
        int id=propertyDAO.save(propertyEntity);
        return findById(id).orElseThrow(()->new EntityNotFoundException("Propriedade não encontrada"));

    }

    public Optional<PropertyEntity> findById(Integer id){
        return propertyDAO.findById(id);
    }

    public List<PropertyEntity> findAllByLandlordId(Integer id){
        return propertyDAO.findAllByLandlordId(id);
    }


}
