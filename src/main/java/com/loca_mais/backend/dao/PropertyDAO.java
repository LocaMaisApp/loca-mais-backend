package com.loca_mais.backend.dao;

import com.loca_mais.backend.exceptions.custom.core.ResourceNotFoundException;
import com.loca_mais.backend.exceptions.util.PostgresError;
import com.loca_mais.backend.model.PropertyEntity;
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
public class PropertyDAO {

        @Autowired
        private DataSource dataSource;

        public int save(PropertyEntity property) {
            String sql = "INSERT INTO properties (name, street, city, state, complement, number, size, bathroom_quantity, suites, car_space, room_quantity, landlord_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setString(1, property.getName());
                stmt.setString(2, property.getStreet());
                stmt.setString(3, property.getCity());
                stmt.setString(4, property.getState());
                stmt.setString(5, property.getComplement());
                stmt.setInt(6, property.getNumber());
                stmt.setInt(7, property.getSize());
                stmt.setInt(8, property.getBathroomQuantity());
                stmt.setInt(9, property.getSuites());
                stmt.setInt(10, property.getCar_space());
                stmt.setInt(11, property.getRoomQuantity());
                stmt.setInt(12, property.getLandlord_id());

                return stmt.executeUpdate();

            } catch (SQLException e) {
                if (PostgresError.isDuplicateKeyError(e)) {
                    throw new DuplicateKeyException("Propriedade duplicada");
                }
                throw new RuntimeException("Erro ao salvar propriedade: " + e.getMessage(), e);
            }
        }

    public void delete(int id) {
        String sql = "DELETE FROM properties WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Propriedade não encontrada para o id: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao apagar propriedade");
        }
    }

    public List<PropertyEntity> findAll() {
        String sql = "SELECT * FROM properties";

        List<PropertyEntity> properties = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    properties.add(mapResultSetToProperty(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as propriedades");
        }
        return properties;
    }


    public Optional<PropertyEntity> findById(int id) {
        String sql = "SELECT * FROM properties WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProperty(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar propriedade por ID", e);
        }
    }


    private PropertyEntity mapResultSetToProperty(ResultSet rs) throws SQLException {
        PropertyEntity property = new PropertyEntity();
        property.setId(rs.getInt("id"));
        property.setCreatedAt(rs.getTimestamp("created_at"));
        property.setUpdatedAt(rs.getTimestamp("updated_at"));
        property.setActive(rs.getBoolean("active"));
        property.setName(rs.getString("name"));
        property.setStreet(rs.getString("street"));
        property.setCity(rs.getString("city"));
        property.setState(rs.getString("state"));
        String complement = rs.getString("complement");
        if(complement != null && !complement.isEmpty()){
            property.setComplement(rs.getString("complement"));
        }
        else{
            property.setComplement(null);
        }
        property.setNumber(rs.getInt("number"));
        property.setSize(rs.getInt("size"));
        property.setBathroomQuantity(rs.getInt("bathroom_quantity"));
        property.setSuites(rs.getInt("suites"));
        property.setCar_space(rs.getInt("car_space"));
        property.setRoomQuantity(rs.getInt("room_quantity"));
        property.setLandlord_id(rs.getInt("landlord_id"));
        return property;
    }
}