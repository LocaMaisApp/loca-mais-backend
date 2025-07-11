package com.loca_mais.backend.mappers;

import com.loca_mais.backend.dto.create.PaymentCreateDTO;
import com.loca_mais.backend.model.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentEntity toEntity(PaymentCreateDTO paymentCreateDTO);

}
