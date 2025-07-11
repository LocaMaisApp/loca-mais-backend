package com.loca_mais.backend.dao;

import com.loca_mais.backend.exceptions.util.PostgresError;
import com.loca_mais.backend.model.AdvertisementImagesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AdvertisementImagesDAO {

    @Autowired
    private DataSource dataSource;

    public int save(AdvertisementImagesEntity advertisementImage) {
        String sql = "INSERT INTO advertisement_images (url, advertisement_id) VALUES (?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, advertisementImage.getUrl());
            stmt.setInt(2, advertisementImage.getAdvertisementId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar imagem do anúncio, nenhuma linha afetada.");
            }
            return affectedRows;
        } catch (SQLException e) {
            if (PostgresError.isDuplicateKeyError(e)) {
                throw new DuplicateKeyException("URL da imagem duplicada ou anúncio já possui esta imagem.");
            }
            throw new RuntimeException("Erro ao salvar imagem do anúncio: " + e.getMessage(), e);
        }
    }

    public Optional<AdvertisementImagesEntity> findById(int id) {
        String sql = "SELECT id, url, advertisement_id, created_at, updated_at, active FROM advertisement_images WHERE advertisement_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAdvertisementImage(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar imagem do anúncio por ID", e);
        }
    }


    public int delete(int id) {
        String sql = "DELETE FROM advertisement_images WHERE advertisement_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar imagem do anúncio: " + e.getMessage(), e);
        }
    }

    public int deleteByUrlAndId(String url,int id) {
        String sql = "DELETE FROM advertisement_images WHERE advertisement_id = ? AND url = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.setString(2, url);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar imagem do anúncio: " + e.getMessage(), e);
        }
    }



    private AdvertisementImagesEntity mapResultSetToAdvertisementImage(ResultSet rs) throws SQLException {
        AdvertisementImagesEntity advertisementImage = new AdvertisementImagesEntity();
        advertisementImage.setUrl(rs.getString("url"));
        advertisementImage.setAdvertisementId(rs.getInt("advertisement_id"));
        return advertisementImage;
    }
}