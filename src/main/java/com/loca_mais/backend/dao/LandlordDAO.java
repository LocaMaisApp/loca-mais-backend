package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.LandlordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LandlordDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<LandlordEntity> landlordRowMapper =
            (rs, rowNum) -> {
                LandlordEntity landlord = new LandlordEntity();
                landlord.setUserId(rs.getInt("User_id"));
                return landlord;
            };


    public int save(LandlordEntity landlord) {
        String sql = "INSERT INTO landlords (user_id) VALUES (?)";
        return jdbcTemplate.update(sql, landlord.getUserId());
    }


    public LandlordEntity findByUserId(int userId) {
        String sql = "SELECT user_id FROM landlords WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, landlordRowMapper, userId);
    }


    public List<LandlordEntity> findAll() {
        String sql = "SELECT user_id FROM landlords";
        return jdbcTemplate.query(sql, landlordRowMapper);
    }


    public int delete(int userId) {
        String sql = "DELETE FROM landlords WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}