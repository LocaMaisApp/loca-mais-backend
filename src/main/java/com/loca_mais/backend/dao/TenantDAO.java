package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.TenantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TenantDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(TenantEntity tenant) {
        String sql = "INSERT INTO tenants (user_id) VALUES (?)";
        return jdbcTemplate.update(sql, tenant.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM tenants WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
