package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.response.UserResponseDTO;
import com.loca_mais.backend.service.LandlordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landlord")
@AllArgsConstructor
public class LandlordController {

    private final LandlordService landlordService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> getLandlordById(@RequestParam int id) {
        UserResponseDTO userDTO = landlordService.getLandlord(id);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }

}
