package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.MaintenanceDAO;
import com.loca_mais.backend.dto.create.CreateMaintenanceDTO;
import com.loca_mais.backend.mappers.MaintenanceMapper;
import com.loca_mais.backend.model.MaintenanceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@AllArgsConstructor
public class MaintenanceService {

    private final MaintenanceDAO maintenanceDAO;
    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceEntity createMaintenance(CreateMaintenanceDTO dto) throws SQLException {
        MaintenanceEntity maintenanceEntity = maintenanceMapper.creationDtoToEntity(dto);
        return maintenanceDAO.save(maintenanceEntity);
    }
}
