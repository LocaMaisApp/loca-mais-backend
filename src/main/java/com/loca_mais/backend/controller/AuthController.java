package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.AuthRegisterDTO;
import com.loca_mais.backend.dto.requests.AuthLoginDTO;
import com.loca_mais.backend.dto.response.AuthLoginResponseDTO;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.mappers.UserMapper;
import com.loca_mais.backend.model.UserEntity;
import com.loca_mais.backend.service.AuthService;
import com.loca_mais.backend.service.JwtService;
import com.loca_mais.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/signIn")
    public ResponseEntity<AuthLoginResponseDTO> signIn(@RequestBody AuthLoginDTO  authLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginDTO.email(),
                        authLoginDTO.password()
                )
        );

        UserEntity user=(UserEntity)authentication.getPrincipal();
        String jwt=jwtService.generateToken(user);
        UserResponseDTO userResponseDTO=userService.signIn(user);
        return ResponseEntity.ok(new AuthLoginResponseDTO(jwt,userResponseDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthLoginResponseDTO> signUp(@RequestBody AuthRegisterDTO authRegisterDTO) {
        authService.signUp(authRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
