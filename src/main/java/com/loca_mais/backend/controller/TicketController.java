package com.loca_mais.backend.controller;

import com.loca_mais.backend.dto.create.CreateTicketDTO;
import com.loca_mais.backend.dto.requests.TicketUpdtateStatusDTO;
import com.loca_mais.backend.mappers.TicketMapper;
import com.loca_mais.backend.model.TicketEntity;
import com.loca_mais.backend.service.TicketService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateTicketDTO createTicketDTO) {
        try {
            TicketEntity newTicket = ticketService.createTicketByTenant(createTicketDTO);
            return ResponseEntity.ok().body(newTicket);
        } catch (SQLException | RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateTicketStatus(@PathVariable int id, @Valid @RequestBody TicketUpdtateStatusDTO updtateStatusDTO) {
        try {
            ticketService.updateTicketStatusByLandlord(id, updtateStatusDTO);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException | AccessDeniedException | SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-tickets/{tenantId}")
    public ResponseEntity<Object> getTenantTickets(@PathVariable int tenantId) {
        try {
            List<TicketEntity> tickets = ticketService.getTicketsByTenant(tenantId);
            return ResponseEntity.ok().body(ticketMapper.entityListToResponseDtoList(tickets));
        } catch (RuntimeException | SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/property/{propertyId}/{landlordId}")
    public ResponseEntity<Object> getPropertyTickets(@PathVariable int propertyId, @PathVariable int landlordId) {
        try {
            List<TicketEntity> tickets = ticketService.getTicketsByProperty(propertyId, landlordId);
            return ResponseEntity.ok().body(ticketMapper.entityListToResponseDtoList(tickets));
        } catch (RuntimeException | AccessDeniedException | SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
