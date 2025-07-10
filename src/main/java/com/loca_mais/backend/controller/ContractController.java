package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.CreateContractDTO;
import com.loca_mais.backend.dto.response.CreateContractResponseDTO;
import com.loca_mais.backend.service.ContractService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contract")
@AllArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateContractDTO createContractDTO) {
        try {
            CreateContractResponseDTO result = this.contractService.execute(createContractDTO);
            return ResponseEntity.ok().body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
