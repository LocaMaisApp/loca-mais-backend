package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.response.AdvertisementResponse;
import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.model.PropertyEntity;
import com.loca_mais.backend.service.LandlordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@AllArgsConstructor
public class LandlordController {

    private final LandlordService landlordService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getLandlordById(@RequestParam int id) {
        UserResponseDTO userDTO = landlordService.findById(id);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{id}/properties")
    public ResponseEntity<List<PropertyEntity>> getLandlordPropertiesById(@PathVariable int id) {
        UserResponseDTO userDTO = landlordService.findById(id);
        List<PropertyEntity> properties=landlordService.findAllLandlordProperties(userDTO.id());
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}/advertisements")
    public ResponseEntity<List<AdvertisementResponse>> getLandlordAdvertisementsById(@PathVariable int id) {
        UserResponseDTO userDTO = landlordService.findById(id);
        List<AdvertisementResponse> advertisements=landlordService.findAllLandlordAdvertisements(userDTO.id());
        return ResponseEntity.ok(advertisements);
    }






}
