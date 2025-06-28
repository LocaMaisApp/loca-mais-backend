package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.UserDTO;
import com.loca_mais.backend.dto.create.UserCreateDTO;
import com.loca_mais.backend.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@AllArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("")
    public ResponseEntity<UserDTO> createTenant(@RequestBody UserCreateDTO user){
        Integer id=tenantService.createTenant(user);
        UserDTO userDTO=tenantService.getTenant(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping
    public ResponseEntity<UserDTO> getTenantById(@RequestParam int id) {
        UserDTO userDTO = tenantService.getTenant(id);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }

}
