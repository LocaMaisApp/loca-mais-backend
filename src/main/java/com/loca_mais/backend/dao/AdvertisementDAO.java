package com.loca_mais.backend.dao;

import com.loca_mais.backend.dto.response.AdvertisementResponse;
import com.loca_mais.backend.exceptions.util.PostgresError;
import com.loca_mais.backend.model.AdvertisementEntity;
import com.loca_mais.backend.model.AdvertisementImagesEntity;
import com.loca_mais.backend.model.PropertyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class AdvertisementDAO {

    @Autowired
    private DataSource dataSource;

    public int save(AdvertisementEntity advertisement) {
        String sql = "INSERT INTO advertisements (description, condominium_value, value, iptu_value, property_id) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, advertisement.getDescription());
            stmt.setDouble(2, advertisement.getCondominiumValue());
            stmt.setDouble(3, advertisement.getValue());
            stmt.setDouble(4, advertisement.getIptuValue());
            stmt.setInt(5, advertisement.getPropertyId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Erro ao criar anuncio.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    advertisement.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Erro ao criar anuncio.");
                }
            }
            return affectedRows;
        } catch (SQLException e) {
            if (PostgresError.isDuplicateKeyError(e)) {
                throw new DuplicateKeyException("Propriedade já associada a um anúncio.");
            }
            throw new RuntimeException("Erro ao salvar anúncio: " + e.getMessage(), e);
        }
    }

    public Optional<AdvertisementResponse> findById(int id) {
        String sql = "SELECT " +
                "a.id AS ad_id, a.description AS ad_description, a.condominium_value AS ad_condominium_value, " +
                "a.value AS ad_value, a.iptu_value AS ad_iptu_value, a.property_id AS ad_property_id, " +
                "a.created_at AS ad_created_at, a.updated_at AS ad_updated_at, a.active AS ad_active, " +
                "p.id AS prop_id, p.name AS prop_name, p.street AS prop_street, p.city AS prop_city, " +
                "p.state AS prop_state, p.complement AS prop_complement, p.number AS prop_number, " +
                "p.size AS prop_size, p.bathroom_quantity AS prop_bathroom_quantity, p.suites AS prop_suites, " +
                "p.car_space AS prop_car_space, p.room_quantity AS prop_room_quantity, p.landlord_id AS prop_landlord_id, " +
                "p.created_at AS prop_created_at, p.updated_at AS prop_updated_at, p.active AS prop_active, " +
                "ai.url AS img_url, ai.advertisement_id AS img_advertisement_id " +
                "FROM advertisements a " +
                "JOIN properties p ON a.property_id = p.id " +
                "LEFT JOIN advertisement_images ai ON a.id = ai.advertisement_id " +
                "WHERE a.id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                AdvertisementResponse advertisementResponse = null;
                AdvertisementEntity tempAdvertisement = null;
                PropertyEntity property = null;
                List<String> imageUrls = new ArrayList<>();

                while (rs.next()) {
                    if (advertisementResponse == null) {
                        tempAdvertisement = mapResultSetToAdvertisement(rs);
                        property = mapResultSetToProperty(rs);

                        advertisementResponse = new AdvertisementResponse(
                                tempAdvertisement.getId(),
                                tempAdvertisement.getDescription(),
                                tempAdvertisement.getCondominiumValue(),
                                tempAdvertisement.getValue(),
                                tempAdvertisement.getIptuValue(),
                                property,
                                imageUrls
                        );
                    }

                    if (rs.getString("img_url") != null) {
                        String imageUrl = rs.getString("img_url");
                        if (!imageUrls.contains(imageUrl)) {
                            imageUrls.add(imageUrl);
                        }
                    }
                }
                return Optional.ofNullable(advertisementResponse);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar anúncio, propriedade e imagens por ID", e);
        }
    }

    public List<AdvertisementResponse> findAll() {
        String sql = "SELECT " +
                "a.id AS ad_id, a.description AS ad_description, a.condominium_value AS ad_condominium_value, " +
                "a.value AS ad_value, a.iptu_value AS ad_iptu_value, a.property_id AS ad_property_id, " +
                "a.created_at AS ad_created_at, a.updated_at AS ad_updated_at, a.active AS ad_active, " +
                "p.id AS prop_id, p.name AS prop_name, p.street AS prop_street, p.city AS prop_city, " +
                "p.state AS prop_state, p.complement AS prop_complement, p.number AS prop_number, " +
                "p.size AS prop_size, p.bathroom_quantity AS prop_bathroom_quantity, p.suites AS prop_suites, " +
                "p.car_space AS prop_car_space, p.room_quantity AS prop_room_quantity, p.landlord_id AS prop_landlord_id, " +
                "p.created_at AS prop_created_at, p.updated_at AS prop_updated_at, p.active AS prop_active, " +
                "ai.url AS img_url, ai.advertisement_id AS img_advertisement_id " +
                "FROM advertisements a " +
                "JOIN properties p ON a.property_id = p.id " +
                "LEFT JOIN advertisement_images ai ON a.id = ai.advertisement_id " +
                "ORDER BY a.id, ai.url"; // Garante que as imagens do mesmo anúncio venham agrupadas

        Map<Integer, AdvertisementResponse> advertisementResponseMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                int adId = rs.getInt("ad_id");
                AdvertisementResponse advertisementResponse = advertisementResponseMap.get(adId);

                if (advertisementResponse == null) {
                    AdvertisementEntity tempAdvertisement = mapResultSetToAdvertisement(rs);
                    PropertyEntity property = mapResultSetToProperty(rs);
                    List<String> imageUrls = new ArrayList<>(); // Nova lista de imagens para este DTO

                    advertisementResponse = new AdvertisementResponse(
                            tempAdvertisement.getId(),
                            tempAdvertisement.getDescription(),
                            tempAdvertisement.getCondominiumValue(),
                            tempAdvertisement.getValue(),
                            tempAdvertisement.getIptuValue(),
                            property,
                            imageUrls
                    );
                    advertisementResponseMap.put(adId, advertisementResponse);
                }

                if (rs.getString("img_url") != null) {
                    String imageUrl = rs.getString("img_url");
                    // Adiciona apenas se a URL da imagem ainda não estiver na lista do DTO
                    if (!advertisementResponse.getImages().contains(imageUrl)) {
                        advertisementResponse.getImages().add(imageUrl);
                    }
                }
            }
            return new ArrayList<>(advertisementResponseMap.values());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todos os anúncios com propriedades e imagens", e);
        }
    }



    public int update(AdvertisementEntity advertisement) {
        String sql = "UPDATE advertisements SET description = ?, condominium_value = ?, value = ?, iptu_value = ?, property_id = ?, updated_at = NOW(), active = ? WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, advertisement.getDescription());
            stmt.setDouble(2, advertisement.getCondominiumValue());
            stmt.setDouble(3, advertisement.getValue());
            stmt.setDouble(4, advertisement.getIptuValue());
            stmt.setInt(5, advertisement.getPropertyId());
            stmt.setBoolean(6, advertisement.isActive());
            stmt.setInt(7, advertisement.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar anúncio: " + e.getMessage(), e);
        }
    }

    public int delete(int id) {
        String sql = "DELETE FROM advertisements WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar anúncio: " + e.getMessage(), e);
        }
    }

    private AdvertisementEntity mapResultSetToAdvertisement(ResultSet rs) throws SQLException {
        AdvertisementEntity advertisement = new AdvertisementEntity();
        advertisement.setId(rs.getInt("ad_id"));
        advertisement.setDescription(rs.getString("ad_description"));
        advertisement.setCondominiumValue(rs.getDouble("ad_condominium_value"));
        advertisement.setValue(rs.getDouble("ad_value"));
        advertisement.setIptuValue(rs.getDouble("ad_iptu_value"));
        advertisement.setPropertyId(rs.getInt("ad_property_id"));
        advertisement.setCreatedAt(rs.getTimestamp("ad_created_at"));
        advertisement.setUpdatedAt(rs.getTimestamp("ad_updated_at"));
        advertisement.setActive(rs.getBoolean("ad_active"));
        return advertisement;
    }

    private PropertyEntity mapResultSetToProperty(ResultSet rs) throws SQLException {
        PropertyEntity property = new PropertyEntity();
        property.setId(rs.getInt("prop_id"));
        property.setName(rs.getString("prop_name"));
        property.setStreet(rs.getString("prop_street"));
        property.setCity(rs.getString("prop_city"));
        property.setState(rs.getString("prop_state"));
        property.setComplement(rs.getString("prop_complement"));
        property.setNumber(rs.getObject("prop_number", Integer.class));
        property.setSize(rs.getObject("prop_size", Integer.class));
        property.setBathroomQuantity(rs.getObject("prop_bathroom_quantity", Integer.class));
        property.setSuites(rs.getObject("prop_suites", Integer.class));
        property.setCar_space(rs.getObject("prop_car_space", Integer.class));
        property.setRoomQuantity(rs.getObject("prop_room_quantity", Integer.class));
        property.setLandlord_id(rs.getObject("prop_landlord_id", Integer.class));
        property.setCreatedAt(rs.getTimestamp("prop_created_at"));
        property.setUpdatedAt(rs.getTimestamp("prop_updated_at"));
        property.setActive(rs.getBoolean("prop_active"));
        return property;
    }

    private AdvertisementImagesEntity mapResultSetToAdvertisementImage(ResultSet rs) throws SQLException {
        AdvertisementImagesEntity advertisementImage = new AdvertisementImagesEntity();
        advertisementImage.setUrl(rs.getString("img_url"));
        advertisementImage.setAdvertisementId(rs.getObject("img_advertisement_id", Integer.class));
        return advertisementImage;
    }
}