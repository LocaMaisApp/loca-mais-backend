package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.AdvertisementDAO;
import com.loca_mais.backend.dao.LandlordDAO;
import com.loca_mais.backend.dao.PropertyDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.AuthRegisterDTO;
import com.loca_mais.backend.dto.response.AdvertisementResponse;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.enums.UserType;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.model.LandlordEntity;
import com.loca_mais.backend.model.PropertyEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LandlordService {

    private final LandlordDAO landlordDAO;
    private final UserDAO userDAO;
    private final PropertyDAO  propertyDAO;
    private final AdvertisementDAO advertisementDAO;

    public void createLandlord(AuthRegisterDTO user,String encodedPassword) {
        UserEntity userEntity = new UserEntity(
                user.name(),
                user.lastName(),
                user.cpf(),
                user.phone(),
                user.email(),
                encodedPassword
        );
        int userId=userDAO.save(userEntity);
        landlordDAO.save(new LandlordEntity(userId));
    }

    public UserResponseDTO findById(Integer id) {
        Optional<UserEntity> optionalUser= landlordDAO.findByUserId(id);
        if(optionalUser.isEmpty()){
            throw new EntityNotFoundException("Proprietário não encontrado");
        }
        UserEntity user=optionalUser.get();

        return new UserResponseDTO(user.getId(),user.getName(),
                user.getLastName(),user.getCpf(),user.getPhone(),user.getEmail(),
                user.getCreatedAt(),user.getUpdatedAt(),user.isActive(), UserType.LANDLORD);
    }


    public List<PropertyEntity> findAllLandlordProperties(Integer landlordId) {
        return propertyDAO.findAllByLandlordId(landlordId);

    }

    public List<AdvertisementResponse> findAllLandlordAdvertisements(Integer landlordId){
        return advertisementDAO.findAllByLandlordId(landlordId);
    }


}
