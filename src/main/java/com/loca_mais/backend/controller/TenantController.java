package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.UserDTO;
import com.loca_mais.backend.dto.create.UserCreateDTO;
import com.loca_mais.backend.model.TenantEntity;
import com.loca_mais.backend.model.UserEntity;
import com.loca_mais.backend.service.TenantService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@AllArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("")
    public ResponseEntity<Void> createTenant(@RequestBody UserCreateDTO user){
        tenantService.createTenant(user);
        return ResponseEntity.ok().build();
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
