package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.PaymentCreateDTO;
import com.loca_mais.backend.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/payment")
@RestController
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<Void> create(@RequestBody PaymentCreateDTO paymentCreateDTO) {
        paymentService.create(paymentCreateDTO);
        return ResponseEntity.ok().build();
    }
}
