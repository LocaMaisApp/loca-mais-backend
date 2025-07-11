package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.response.ContractResponseDTO;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant")
@AllArgsConstructor
public class TenantController {

    private final TenantService tenantService;


    @GetMapping
    public ResponseEntity<UserResponseDTO> findTenantById(@RequestParam int id) {
        UserResponseDTO userDTO = tenantService.findById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping({"/{id}/contracts"})
    public ResponseEntity<List<ContractResponseDTO>> findAllTenantContracts(@PathVariable("id") Integer id) {
        List<ContractResponseDTO> contracts=tenantService.findAllTenantContracts(id);
        return ResponseEntity.ok(contracts);
    }


}
