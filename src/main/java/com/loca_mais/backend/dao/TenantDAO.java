package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.TenantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class TenantDAO {

    @Autowired
    private DataSource dataSource;

    public int save(TenantEntity tenant) {
        String sql = "INSERT INTO tenants (user_id) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, tenant.getUserId());
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar tenant", e);
        }
    }

    public int delete(int userId) {
        String sql = "DELETE FROM tenants WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar tenant", e);
        }
    }
}
