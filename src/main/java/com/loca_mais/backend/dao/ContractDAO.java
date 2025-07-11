package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.ContractEntity;
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
}