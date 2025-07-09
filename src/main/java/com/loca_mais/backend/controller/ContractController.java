package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.CreateContractDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contract")
public class ContractController {

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody CreateContractDTO createContractDTO) {

    }

}
