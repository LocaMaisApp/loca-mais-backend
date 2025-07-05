package com.loca_mais.backend.dao;

import com.loca_mais.backend.exceptions.custom.core.ResourceNotFoundException;
import com.loca_mais.backend.exceptions.util.PostgresError;
import com.loca_mais.backend.model.TenantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
        if (PostgresError.isDuplicateKeyError(e)) {
            throw new DuplicateKeyException("Inquilino duplicado");
        }
        throw new RuntimeException("Erro ao salvar inquilino: " + e.getMessage(), e);
    }
    }

    public void delete(int userId) {
        String sql = "DELETE FROM tenants WHERE user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Inquilino não encontrado para o id: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar tenant", e);
        }
    }
}
