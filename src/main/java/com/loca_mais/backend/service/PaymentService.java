package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.PaymentDAO;
import com.loca_mais.backend.dto.create.PaymentCreateDTO;
import com.loca_mais.backend.mappers.PaymentMapper;
import com.loca_mais.backend.model.PaymentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final PaymentMapper paymentMapper;

    public void create(PaymentCreateDTO paymentCreateDTO) {
        PaymentEntity paymentEntity = paymentMapper.toEntity(paymentCreateDTO);
        paymentDAO.save(paymentEntity);
    }



}
