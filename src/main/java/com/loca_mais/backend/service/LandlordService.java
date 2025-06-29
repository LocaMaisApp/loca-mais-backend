package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.LandlordDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.UserCreateDTO;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.enums.UserType;
import com.loca_mais.backend.model.LandlordEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LandlordService {

    private final LandlordDAO landlordDAO;
    private final UserDAO userDAO;

    public Integer createLandlord(UserCreateDTO user) {
        UserEntity userEntity = new UserEntity(
                user.name(),
                user.lastName(),
                user.cpf(),
                user.phone(),
                user.email(),
                user.password()
        );
        int userId=userDAO.save(userEntity);
        return landlordDAO.save(new LandlordEntity(userId));
    }

    public UserResponseDTO getLandlord(Integer id) {
        UserEntity user= userDAO.findById(id);

        return new UserResponseDTO(user.getId(),user.getName(),
                user.getLastName(),user.getCpf(),user.getPhone(),user.getEmail(),
                user.getCreatedAt(),user.getUpdatedAt(),user.isActive(), UserType.LANDLORD);
    }



}
