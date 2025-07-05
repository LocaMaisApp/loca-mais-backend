package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.AuthRegisterDTO;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.enums.UserType;
import com.loca_mais.backend.mappers.UserMapper;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final UserMapper userMapper;


    public UserEntity findUserByEmailOrCpf(String email,String cpf) {
        return userDAO.findByEmailOrCpf(email,cpf).orElse(null);
    }

    public UserResponseDTO signIn(UserEntity userEntity) {
        UserType type=userDAO.isTenant(userEntity.getId())?UserType.TENANT:UserType.LANDLORD;
        UserResponseDTO response=userMapper.toUserResponseDTO(userEntity,type);
        return response;
    }

    public Optional<UserEntity> findById(int id){
        return userDAO.findById(id);
    }


}
