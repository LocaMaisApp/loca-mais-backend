package com.loca_mais.backend.dao;

import com.loca_mais.backend.dto.response.MaintenanceResponseDTO;
import com.loca_mais.backend.enums.TickerStatus;
import com.loca_mais.backend.model.MaintenanceEntity;
import com.loca_mais.backend.model.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MaintenanceDAO {

    @Autowired
    private DataSource dataSource;

    public MaintenanceEntity create(MaintenanceEntity maintenanceEntity) {
        String sql = "INSERT INTO maintenances (total_value, ticket_id, created_at, updated_at, finished_at) VALUES (?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            statement.setBigDecimal(1, maintenanceEntity.getTotal_value());
            statement.setInt(2, maintenanceEntity.getTicket_id());
            statement.setTimestamp(3, currentTimestamp); // created_at
            statement.setTimestamp(4, currentTimestamp); // updated_at
            statement.setTimestamp(5, new Timestamp(maintenanceEntity.getFinished_at().getTime()));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    maintenanceEntity.setId(rs.getInt("id"));
                    maintenanceEntity.setCreated_at(rs.getTimestamp("created_at"));
                    maintenanceEntity.setUpdated_at(rs.getTimestamp("updated_at"));
                    return maintenanceEntity;
                } else {
                    throw new SQLException("Failed to create maintenance, no data returned.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving maintenance to the database", e);
        }
    }

    public List<MaintenanceResponseDTO> findAllByUserId(int propertyId) {
        String sql = "SELECT " +
                "m.id AS maintenance_id, m.created_at AS maintenance_created_at, m.updated_at AS maintenance_updated_at, " +
                "m.finished_at AS maintenance_finished_at, m.total_value, " +
                "t.id AS ticket_id, t.created_at AS ticket_created_at, t.updated_at AS ticket_updated_at, " +
                "t.urgent AS ticket_urgent, t.description AS ticket_description, t.status AS ticket_status, t.tenant_id AS ticket_tenant_id, " +
                "p.id AS property_id, p.created_at AS property_created_at, p.updated_at AS property_updated_at, " +
                "p.active AS property_active, p.name AS property_name, p.street AS property_street, " +
                "p.size, p.bathroom_quantity, p.state, p.suites, p.car_space, p.complement, p.number, " +
                "p.room_quantity, p.city, p.landlord_id " +
                "FROM maintenances m " +
                "JOIN tickets t ON m.ticket_id = t.id " +
                "JOIN properties p ON t.property_id = p.id " +
                "WHERE p.landlord_id = ? " +
                "ORDER BY m.created_at DESC";

        List<MaintenanceResponseDTO> maintenances = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, propertyId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    PropertyEntity property = mapRowToPropertyEntity(rs);

                    MaintenanceResponseDTO.TicketDTO ticketDTO = MaintenanceResponseDTO.TicketDTO.builder()
                            .id(rs.getInt("ticket_id"))
                            .created_at(getLocalDateTimeOrNull(rs.getTimestamp("ticket_created_at")))
                            .updated_at(getLocalDateTimeOrNull(rs.getTimestamp("ticket_updated_at")))
                            .urgent(rs.getBoolean("ticket_urgent"))
                            .description(rs.getString("ticket_description"))
                            .status(TickerStatus.valueOf(rs.getString("ticket_status")))
                            .property(property)
                            .tenant_id(rs.getInt("ticket_tenant_id"))
                            .build();

                    MaintenanceResponseDTO maintenanceDTO = MaintenanceResponseDTO.builder()
                            .id(rs.getInt("maintenance_id"))
                            .created_at(getLocalDateTimeOrNull(rs.getTimestamp("maintenance_created_at")))
                            .updated_at(getLocalDateTimeOrNull(rs.getTimestamp("maintenance_updated_at")))
                            .finished_at(getLocalDateTimeOrNull(rs.getTimestamp("maintenance_finished_at")))
                            .total_value(rs.getBigDecimal("total_value"))
                            .ticket(ticketDTO)
                            .build();

                    maintenances.add(maintenanceDTO);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching maintenances by property ID", e);
        }
        return maintenances;
    }

    public Optional<MaintenanceEntity> findById(int id) {
        String sql = "SELECT * FROM maintenances WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToMaintenanceEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching maintenance from the database", e);
        }
        return Optional.empty();
    }

    public void update(MaintenanceEntity maintenanceEntity) {
        String sql = "UPDATE maintenances SET total_value = ?, ticket_id = ?, updated_at = ?, finished_at = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, maintenanceEntity.getTotal_value());
            statement.setInt(2, maintenanceEntity.getTicket_id());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Update updated_at
            statement.setTimestamp(4, new Timestamp(maintenanceEntity.getFinished_at().getTime()));
            statement.setInt(5, maintenanceEntity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating maintenance in the database", e);
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM maintenances WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting maintenance from the database", e);
        }
    }

    private MaintenanceEntity mapRowToMaintenanceEntity(ResultSet rs) throws SQLException {
        return MaintenanceEntity.builder()
                .id(rs.getInt("id"))
                .created_at(rs.getTimestamp("created_at"))
                .updated_at(rs.getTimestamp("updated_at"))
                .finished_at(rs.getTimestamp("finished_at"))
                .total_value(rs.getBigDecimal("total_value"))
                .ticket_id(rs.getInt("ticket_id"))
                .build();
    }

    private PropertyEntity mapRowToPropertyEntity(ResultSet rs) throws SQLException {
        PropertyEntity property = new PropertyEntity();
        property.setId(rs.getInt("property_id"));
        property.setCreatedAt(rs.getTimestamp("property_created_at"));
        property.setUpdatedAt(getTimestampOrNull(rs.getTimestamp("property_updated_at")));
        property.setActive(rs.getBoolean("property_active"));
        property.setName(rs.getString("property_name"));
        property.setStreet(rs.getString("property_street"));
        property.setCity(rs.getString("city"));
        property.setState(rs.getString("state"));
        property.setComplement(rs.getString("complement"));
        property.setNumber(rs.getInt("number"));
        property.setSize(rs.getInt("size"));
        property.setSuites(rs.getInt("suites"));
        property.setBathroomQuantity(rs.getInt("bathroom_quantity"));
        property.setCar_space(rs.getInt("car_space"));
        property.setRoomQuantity(rs.getInt("room_quantity"));
        property.setLandlord_id(rs.getInt("landlord_id"));
        return property;
    }

    private LocalDateTime getLocalDateTimeOrNull(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    private Timestamp getTimestampOrNull(Timestamp timestamp) {
        return timestamp;
    }
}