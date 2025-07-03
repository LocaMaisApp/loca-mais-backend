package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@AllArgsConstructor
public class TenantController {

    private final TenantService tenantService;


    @GetMapping
    public ResponseEntity<UserResponseDTO> getTenantById(@RequestParam int id) {
        UserResponseDTO userDTO = tenantService.getTenant(id);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }

}
