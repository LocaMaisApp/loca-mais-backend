package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<UserEntity> userRowMapper = new RowMapper<UserEntity>() {
        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
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

    public int save(UserEntity user) {
        String sql = "INSERT INTO users (name, last_name, cpf, phone, email, password, created_at, updated_at, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID";

        Integer id = jdbcTemplate.queryForObject(sql,
                Integer.class,
                user.getName(),
                user.getLastName(),
                user.getCpf(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                new java.sql.Timestamp(user.getCreatedAt().getTime()),
                new java.sql.Timestamp(user.getUpdatedAt().getTime()),
                user.isActive()
        );

        if (id == null) {
            throw new RuntimeException("Erro ao inserir usuário: id retornado é null");
        }

        return id;
    }

    public Optional<UserEntity> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            UserEntity user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> findByEmailOrCpf(String email,String cpf) {
        String sql = "SELECT * FROM users WHERE email = ? OR cpf=?";
        try {
            UserEntity user = jdbcTemplate.queryForObject(sql, userRowMapper, email,cpf);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public boolean isTenant(int userId) {
        String sql = "SELECT COUNT(*) FROM tenants WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public boolean isLandlord(int userId) {
        String sql = "SELECT COUNT(*) FROM landlords WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }


    public int update(UserEntity user) {
        String sql = "UPDATE users SET name = ?, last_name = ?, cpf = ?, phone = ?, email = ?, password = ?, updated_at = ?, active = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getLastName(),
                user.getCpf(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                new java.sql.Timestamp(user.getUpdatedAt().getTime()),
                user.isActive(),
                user.getId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
