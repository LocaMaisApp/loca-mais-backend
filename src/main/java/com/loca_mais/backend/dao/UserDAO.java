package com.loca_mais.backend.dao;

import com.loca_mais.backend.exceptions.custom.core.ResourceNotFoundException;
import com.loca_mais.backend.exceptions.util.PostgresError;
import com.loca_mais.backend.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAO {

    @Autowired
    private DataSource dataSource;

    public int save(UserEntity user) {
        String sql = "INSERT INTO users (name, last_name, cpf, phone, email, password, created_at, updated_at, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getCpf());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setTimestamp(7, new Timestamp(user.getCreatedAt().getTime()));
            stmt.setTimestamp(8, new Timestamp(user.getUpdatedAt().getTime()));
            stmt.setBoolean(9, user.isActive());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Falha ao obter ID gerado.");
                }
            }
        } catch (SQLException e) {
        if (PostgresError.isDuplicateKeyError(e)) {
            throw new DuplicateKeyException("Usuário duplicado");
        }
        throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
    }
    }

    public Optional<UserEntity> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID", e);
        }
    }

    public Optional<UserEntity> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        }
    }

    public Optional<UserEntity> findByEmailOrCpf(String email, String cpf) {
        String sql = "SELECT * FROM users WHERE email = ? OR cpf = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, email);
            stmt.setString(2, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por email ou CPF", e);
        }
    }

    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM users";
        List<UserEntity> users = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os usuários", e);
        }
    }

    public int update(UserEntity user) {
        String sql = "UPDATE users SET name = ?, last_name = ?, cpf = ?, phone = ?, email = ?, password = ?, updated_at = ?, active = ? WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getCpf());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setTimestamp(7, new Timestamp(user.getUpdatedAt().getTime()));
            stmt.setBoolean(8, user.isActive());
            stmt.setInt(9, user.getId());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            if (PostgresError.isDuplicateKeyError(e)) {
                throw new DuplicateKeyException("Usuário duplicado");
            }
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Usuário não encontrado para o id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }

    public boolean isTenant(int userId) {
        String sql = "SELECT COUNT(*) FROM tenants WHERE user_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se é inquilino", e);
        }
    }

    public boolean isLandlord(int userId) {
        String sql = "SELECT COUNT(*) FROM landlords WHERE user_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se é locador", e);
        }
    }

    private UserEntity mapResultSetToUser(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setLastName(rs.getString("last_name"));
        user.setCpf(rs.getString("cpf"));
        user.setPhone(rs.getString("phone"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setActive(rs.getBoolean("active"));
        return user;
    }
}
