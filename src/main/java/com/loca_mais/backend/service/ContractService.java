package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.ContractDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractDAO contractDAO;

}
