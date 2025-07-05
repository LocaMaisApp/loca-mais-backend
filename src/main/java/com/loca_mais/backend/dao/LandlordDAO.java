package com.loca_mais.backend.dao;

import com.loca_mais.backend.exceptions.util.PostgresError;
import com.loca_mais.backend.model.LandlordEntity;
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
public class LandlordDAO {

    @Autowired
    private DataSource dataSource;

    public int save(LandlordEntity landlord) {
        String sql = "INSERT INTO landlords (user_id) VALUES (?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, landlord.getUserId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            if (PostgresError.isDuplicateKeyError(e)) {
                throw new DuplicateKeyException("Proprietário duplicado");
            }
            throw new RuntimeException("Erro ao salvar proprietário: " + e.getMessage(), e);
        }
    }

    public Optional<UserEntity> findByUserId(int userId) {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "JOIN landlords l ON l.user_id = u.id " +
                "WHERE l.user_id = ?;";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar landlord por user_id", e);
        }
    }

    public List<LandlordEntity> findAll() {
        String sql = "SELECT user_id FROM landlords";
        List<LandlordEntity> landlords = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                LandlordEntity landlord = new LandlordEntity();
                landlord.setUserId(rs.getInt("user_id"));
                landlords.add(landlord);
            }
            return landlords;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os landlords", e);
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
};
