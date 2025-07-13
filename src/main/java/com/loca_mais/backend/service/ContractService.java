package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.ContractDAO;
import com.loca_mais.backend.dao.MaintenanceDAO;
import com.loca_mais.backend.dao.PropertyDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.CreateContractDTO;
import com.loca_mais.backend.dto.response.CreateContractResponseDTO;
import com.loca_mais.backend.dto.response.ContractResponseDTO;
import com.loca_mais.backend.dto.response.MaintenanceResponseDTO;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.mappers.ContractMapper;
import com.loca_mais.backend.model.ContractEntity;
import com.loca_mais.backend.model.PropertyEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractDAO contractDAO;
    private final UserDAO userDAO;
    private final PropertyDAO propertyDAO;
    private final ContractMapper contractMapper;
    private final MaintenanceDAO maintenanceDAO;

    public CreateContractResponseDTO create(CreateContractDTO createContractDTO) {
        Optional<UserEntity> tenantOptional = userDAO.findByEmail(createContractDTO.tenantEmail());

        if(tenantOptional.isEmpty()) {
            throw new EntityNotFoundException("Inquilino não encontrado.");
        }

        UserEntity tenant = tenantOptional.get();

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

        ContractEntity createdContract = contractDAO.create(contractEntity);

        return contractMapper.toCreateContractResponseDTO(createdContract);
    }

    public ContractResponseDTO findById(int id) {
        return contractDAO.findWithPropertyById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado", HttpStatus.NOT_FOUND));
     }

     public List<MaintenanceResponseDTO> findAllMaintenancesByUserId(Integer id) {
        return maintenanceDAO.findAllByUserId(id);
     }


     public void deleteById(int id) {
        contractDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado", HttpStatus.NOT_FOUND));

        contractDAO.softDeleteById(id);
     }

}
