package com.loca_mais.backend.service;

import com.loca_mais.backend.dto.create.AuthRegisterDTO;
import com.loca_mais.backend.enums.UserType;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final LandlordService landlordService;
    private final TenantService tenantService;
    private final UserService userService;


    public void signUp(AuthRegisterDTO authRegisterDTO) {
        if(userService.findUserByEmailOrCpf(authRegisterDTO.email(),authRegisterDTO.cpf())!=null){
            throw new RuntimeException("Email ou CPF já existente");
        }
        String encodedPassword = passwordEncoder.encode(authRegisterDTO.password());
        if(authRegisterDTO.type()== UserType.TENANT){
            tenantService.createTenant(authRegisterDTO,encodedPassword);
        }
        else if(authRegisterDTO.type()==UserType.LANDLORD){
            landlordService.createLandlord(authRegisterDTO,encodedPassword);
        }
    }

}
