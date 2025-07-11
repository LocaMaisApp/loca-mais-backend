package com.loca_mais.backend.dao;

import com.loca_mais.backend.dto.response.GetContractResponseDTO;
import com.loca_mais.backend.model.ContractEntity;
import com.loca_mais.backend.model.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class ContractDAO {

    @Autowired
    private DataSource dataSource;

    public ContractEntity create(ContractEntity contractEntity) {
        String sql = "INSERT INTO contracts (payment_day, monthly_value, duration, deposit, property_id, tenant_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id, created_at";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, contractEntity.getPayment_day());
            statement.setBigDecimal(2, contractEntity.getMonthly_value());
            statement.setInt(3, contractEntity.getDuration());
            statement.setBigDecimal(4, contractEntity.getDeposit());
            statement.setInt(5, contractEntity.getProperty_id());
            statement.setInt(6, contractEntity.getTenant_id());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    contractEntity.setId(rs.getInt("id"));
                    contractEntity.setCreatedAt(rs.getTimestamp("created_at"));
                    return contractEntity;
                } else {
                    throw new SQLException("A criação do contrato falhou, nenhum dado retornado.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar contrato no banco de dados", e);
        }
    }

    public Optional<GetContractResponseDTO> findWithPropertyById(int id) {
        String sql = "SELECT " +
                "c.id AS contract_id, c.created_at AS contract_created_at, c.updated_at AS contract_updated_at, " +
                "c.active AS contract_active, c.payment_day, c.monthly_value, c.duration, c.deposit, c.tenant_id, " +
                "p.id AS property_id, p.created_at AS property_created_at, p.updated_at AS property_updated_at, " +
                "p.active AS property_active, p.name, p.street, p.size, p.bathroom_quantity, p.state, " +
                "p.suites, p.car_space, p.complement, p.number, p.room_quantity, p.city, p.landlord_id " +
                "FROM contracts c " +
                "JOIN properties p ON c.property_id = p.id " +
                "WHERE c.id = ? AND c.active = TRUE";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToGetContractResponseDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o contrato com a propriedade no banco de dados", e);
        }
        return Optional.empty();
    }


    public Optional<ContractEntity> findById(int id) {
        String sql = "SELECT * FROM contracts WHERE id = ? AND active = TRUE";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToContractEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o contrato no banco de dados", e);
        }
        return Optional.empty();
    }

    public void softDeleteById(int id) {
        String sql = "UPDATE contracts SET active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar o contrato no banco de dados", e);
        }
    }

    private ContractEntity mapRowToContractEntity(ResultSet rs) throws SQLException {
        return ContractEntity.builder()
                .id(rs.getInt("id"))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .active(rs.getBoolean("active"))
                .payment_day(rs.getInt("payment_day"))
                .monthly_value(rs.getBigDecimal("monthly_value"))
                .duration(rs.getInt("duration"))
                .deposit(rs.getBigDecimal("deposit"))
                .property_id(rs.getInt("property_id"))
                .tenant_id(rs.getInt("tenant_id"))
                .build();
    }

    private PropertyEntity mapRowToPropertyEntity(ResultSet rs) throws SQLException {
        PropertyEntity property = new PropertyEntity();
        property.setId(rs.getInt("property_id"));
        property.setCreatedAt(rs.getTimestamp("property_created_at"));
        property.setUpdatedAt(rs.getTimestamp("property_updated_at"));
        property.setActive(rs.getBoolean("property_active"));
        property.setName(rs.getString("name"));
        property.setStreet(rs.getString("street"));
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

    private GetContractResponseDTO mapRowToGetContractResponseDTO(ResultSet rs) throws SQLException {
        PropertyEntity property = mapRowToPropertyEntity(rs);
        Timestamp updatedAtTimestamp = rs.getTimestamp("contract_updated_at");

        return new GetContractResponseDTO(
                rs.getInt("contract_id"),
                rs.getTimestamp("contract_created_at").toLocalDateTime(),
                updatedAtTimestamp != null ? updatedAtTimestamp.toLocalDateTime() : null,
                rs.getInt("payment_day"),
                rs.getBigDecimal("monthly_value"),
                rs.getInt("duration"),
                rs.getBigDecimal("deposit"),
                rs.getInt("tenant_id"),
                rs.getBoolean("contract_active"),
                property
        );
    }
}