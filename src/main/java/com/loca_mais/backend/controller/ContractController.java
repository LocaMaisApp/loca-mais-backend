package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.CreateContractDTO;
import com.loca_mais.backend.dto.response.CreateContractResponseDTO;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.model.ContractEntity;
import com.loca_mais.backend.service.ContractService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
@AllArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateContractDTO createContractDTO) {
        try {
            CreateContractResponseDTO result = this.contractService.create(createContractDTO);
            return ResponseEntity.ok().body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getContractById(@PathVariable int id) {
        try {
            ContractEntity result = contractService.findById(id);
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        try {
            contractService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
