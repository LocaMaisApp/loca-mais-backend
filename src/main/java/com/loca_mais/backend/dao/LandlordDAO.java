package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.LandlordEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new RuntimeException("Erro ao salvar landlord", e);
        }
    }

    public Optional<LandlordEntity> findByUserId(int userId) {
        String sql = "SELECT user_id FROM landlords WHERE user_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LandlordEntity landlord = new LandlordEntity();
                    landlord.setUserId(rs.getInt("user_id"));
                    return Optional.of(landlord);
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

};
