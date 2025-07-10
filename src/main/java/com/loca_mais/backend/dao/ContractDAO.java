package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.ContractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

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
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            throw new RuntimeException("Erro ao salvar contrato no banco de dados", e);
        }
    }
}