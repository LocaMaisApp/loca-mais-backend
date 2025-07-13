package com.loca_mais.backend.dao;

import com.loca_mais.backend.enums.TickerStatus;
import com.loca_mais.backend.model.TicketEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDAO {

    @Autowired
    private DataSource dataSource;

    public TicketEntity save(TicketEntity ticket) throws SQLException {
        String sql = "INSERT INTO locamais.tickets (urgent, description, status, property_id, tenant_id, created_at, updated_at, active) VALUES (?, ?, ?::ticket_status_enum, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setBoolean(1, ticket.isUrgent());
            statement.setString(2, ticket.getDescription());
            statement.setString(3, ticket.getStatus().name());
            statement.setInt(4, ticket.getProperty_id());
            statement.setInt(5, ticket.getTenant_id());
            statement.setTimestamp(6, new Timestamp(ticket.getCreatedAt().getTime()));
            statement.setTimestamp(7, new Timestamp(ticket.getUpdatedAt().getTime()));
            statement.setBoolean(8, ticket.isActive());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setId(generatedKeys.getInt(1));
                }
            }
        }
        return ticket;
    }

    public Optional<TicketEntity> findById(int id) throws SQLException {
        String sql = "SELECT * FROM locamais.tickets WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTicket(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<TicketEntity> findByTenantId(int tenantId) throws SQLException {
        List<TicketEntity> tickets = new ArrayList<>();

        String sql = "SELECT * FROM locamais.tickets WHERE tenant_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tenantId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        }
        return tickets;
    }

    public List<TicketEntity> findByPropertyId(int propertyId) throws SQLException {
        List<TicketEntity> tickets = new ArrayList<>();

        String sql = "SELECT * FROM locamais.tickets WHERE property_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, propertyId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        }
        return tickets;
    }

    public void updateStatus(int id, TickerStatus status) throws SQLException {
        String sql = "UPDATE locamais.tickets SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    private TicketEntity mapRowToTicket(ResultSet rs) throws SQLException {
        return TicketEntity.builder()
                .id(rs.getInt("id"))
                .urgent(rs.getBoolean("urgent"))
                .description(rs.getString("description"))
                .status(TickerStatus.valueOf(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .active(rs.getBoolean("active"))
                .property_id(rs.getInt("property_id"))
                .tenant_id(rs.getInt("tenant_id"))
                .build();
    }
}
