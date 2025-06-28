package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.TenantDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.UserDTO;
import com.loca_mais.backend.dto.create.UserCreateDTO;
import com.loca_mais.backend.model.TenantEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TenantService {

    private final TenantDAO tenantDAO;
    private final UserDAO userDAO;

    public Integer createTenant(UserCreateDTO user) {
        UserEntity userEntity = new UserEntity(
                user.name(),
                user.lastName(),
                user.cpf(),
                user.phone(),
                user.email(),
                user.password()
        );
        int userId=userDAO.save(userEntity);
        return tenantDAO.save(new TenantEntity(userId));
    }

    public UserDTO getTenant(Integer id) {
        UserEntity user= userDAO.findById(id);

        return new UserDTO(user.getId(),user.getName(),
                user.getLastName(),user.getCpf(),user.getPhone(),user.getEmail(),
                user.getCreatedAt(),user.getUpdatedAt(),user.isActive());
    }



}
