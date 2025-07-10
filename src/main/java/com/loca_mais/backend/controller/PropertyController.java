package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.PropertyRegistrationDTO;
import com.loca_mais.backend.model.PropertyEntity;
import com.loca_mais.backend.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property")
@AllArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyEntity>> findAll() {
        List<PropertyEntity> properties=propertyService.findAll();
        return ResponseEntity.ok(properties);
    }

    @PostMapping
    public ResponseEntity<PropertyEntity> create(@RequestBody PropertyRegistrationDTO registrationDTO){
       PropertyEntity property=propertyService.create(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }

}
