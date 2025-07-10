package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.ContractDAO;
import com.loca_mais.backend.dao.PropertyDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.CreateContractDTO;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.model.ContractEntity;
import com.loca_mais.backend.model.PropertyEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractDAO contractDAO;
    private final UserDAO userDAO;
    private final PropertyDAO propertyDAO;

    public ContractEntity execute(CreateContractDTO createContractDTO) {
        Optional<UserEntity> userOptional = userDAO.findByEmail(createContractDTO.Tenant_User_email());

        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException("Inquilino não encontrado.");
        }

        UserEntity tenant = userOptional.get();

        Optional<PropertyEntity> propertyOptional = propertyDAO.findById(createContractDTO.property_id());

        if(propertyOptional.isEmpty()) {
            throw new EntityNotFoundException("Propriedade não encontrada.");
        }

        PropertyEntity property = propertyOptional.get();

        ContractEntity contractEntity = ContractEntity.builder()
                .payment_day(createContractDTO.payment_day())
                .monthly_value(createContractDTO.monthly_value())
                .duration(createContractDTO.duration())
                .deposit(createContractDTO.deposit())
                .property_id(property.getId())
                .tenant_id(tenant.getId())
                .build();

        return contractDAO.create(contractEntity);
    }

}
