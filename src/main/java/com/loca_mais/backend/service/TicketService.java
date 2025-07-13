package com.loca_mais.backend.service;

import com.loca_mais.backend.dao.PropertyDAO;
import com.loca_mais.backend.dao.TicketDAO;
import com.loca_mais.backend.dao.UserDAO;
import com.loca_mais.backend.dto.create.CreateMaintenanceDTO;
import com.loca_mais.backend.dto.create.CreateTicketDTO;
import com.loca_mais.backend.dto.requests.TicketUpdtateStatusDTO;
import com.loca_mais.backend.enums.TickerStatus;
import com.loca_mais.backend.exceptions.custom.core.BusinessException;
import com.loca_mais.backend.exceptions.custom.core.EntityNotFoundException;
import com.loca_mais.backend.mappers.TicketMapper;
import com.loca_mais.backend.model.PropertyEntity;
import com.loca_mais.backend.model.TicketEntity;
import com.loca_mais.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketDAO ticketDAO;
    private final MaintenanceService maintenanceService;
    private final PropertyDAO propertyDAO;
    private final TicketMapper ticketMapper;
    private final UserDAO userDAO;

    public TicketEntity createTicketByTenant(CreateTicketDTO dto) throws SQLException {
        propertyDAO.findById(dto.property_id())
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada."));

        Optional<UserEntity> tenantEntity = userDAO.findByEmail(dto.tenantEmail());
        UserEntity tenant = tenantEntity.get();

        TicketEntity ticket = ticketMapper.creationDtoToEntity(dto);

        Date now = new Date();
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        ticket.setTenant_id(tenant.getId());
        ticket.setStatus(TickerStatus.PENDENT);
        ticket.setActive(true);

        return ticketDAO.save(ticket);
    }

    public void updateTicketStatusByLandlord(int ticketId, TicketUpdtateStatusDTO dto, String email) throws SQLException, AccessDeniedException {
        TicketEntity ticket = ticketDAO.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado."));

        PropertyEntity property = propertyDAO.findById(ticket.getProperty_id())
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada."));

        Optional<UserEntity> landlordEntity = userDAO.findByEmail(email);
        UserEntity landlord = landlordEntity.get();

        if(property.getLandlord_id() != landlord.getId()) {
            throw new AccessDeniedException("Você não tem permissão para gerenciar este ticket.");
        }

        validateStatusTransition(ticket.getStatus(), dto.status());

        ticketDAO.updateStatus(ticketId, dto.status());

        if (dto.status() == TickerStatus.FINISHED) {
            if (dto.total_value() == null) {
                throw new BusinessException("O valor total da manutenção é obrigatório para finalizar o ticket.");
            }

            CreateMaintenanceDTO maintenanceDTO = new CreateMaintenanceDTO(
                    dto.total_value(),
                    new Date(),
                    ticketId
            );

            maintenanceService.createMaintenance(maintenanceDTO);
        }
    }

    public List<TicketEntity> getTicketsByTenant(int tenantId) throws SQLException {
        return ticketDAO.findByTenantId(tenantId);
    }

    public List<TicketEntity> getTicketsByProperty(int propertyId, int landlordId) throws AccessDeniedException, SQLException {
        PropertyEntity property = propertyDAO.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Propriedade não encontrada."));

        if(property.getLandlord_id() != landlordId) {
            throw new AccessDeniedException("Você não tem permissão para visualizar os tickets desta propriedade.");
        }

        return ticketDAO.findByPropertyId(propertyId);
    }

    private void validateStatusTransition(TickerStatus currentStatus, TickerStatus newStatus) {
        if (currentStatus == TickerStatus.PENDENT && newStatus != TickerStatus.PROGRESS) {
            throw new BusinessException("Um ticket PENDENTE só pode ser movido para EM PROGRESSO.");
        }
        if (currentStatus == TickerStatus.PROGRESS && newStatus != TickerStatus.FINISHED) {
            throw new BusinessException("Um ticket EM PROGRESSO só pode ser movido para FINALIZADO.");
        }
        if (currentStatus == TickerStatus.FINISHED) {
            throw new BusinessException("Um ticket FINALIZADO não pode ter seu status alterado.");
        }
    }
}
