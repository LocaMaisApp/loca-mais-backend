package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.ContractDAO;
import com.loca_mais.backend.dao.TenantDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.AuthRegisterDTO;
import com.loca_mais.backend.dto.response.ContractResponseDTO;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.enums.UserType;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.model.TenantEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TenantService {

    private final TenantDAO tenantDAO;
    private final UserDAO userDAO;
    private final ContractDAO contractDAO;

    public Integer createTenant(AuthRegisterDTO user,String encodedPassword) {
        UserEntity userEntity = new UserEntity(
                user.name(),
                user.lastName(),
                user.cpf(),
                user.phone(),
                user.email(),
                encodedPassword
        );
        int userId=userDAO.save(userEntity);
        return tenantDAO.save(new TenantEntity(userId));
    }

    public UserResponseDTO findById(Integer id) {
        Optional<UserEntity> optionalUser= userDAO.findById(id);
        if(optionalUser.isEmpty()){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        UserEntity user=optionalUser.get();
        return new UserResponseDTO(user.getId(),user.getName(),
                user.getLastName(),user.getCpf(),user.getPhone(),user.getEmail(),
                user.getCreatedAt(),user.getUpdatedAt(),user.isActive(), UserType.TENANT);
    }

    public List<ContractResponseDTO> findAllTenantContracts(Integer id){
        return contractDAO.findAllByTenantId(id);
    }



}
