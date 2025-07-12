package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.MaintenanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class MaintenanceDAO {

    @Autowired
    private DataSource dataSource;

    public MaintenanceEntity save(MaintenanceEntity maintenance) throws SQLException {
        String sql = "INSERT INTO locamais.maintenances (created_at, updated_at, finished_at, total_value, ticket_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setTimestamp(1, new Timestamp(maintenance.getCreated_at().getTime()));
            statement.setTimestamp(2, new Timestamp(maintenance.getUpdated_at().getTime()));
            statement.setTimestamp(3, new Timestamp(maintenance.getFinished_at().getTime()));
            statement.setBigDecimal(4, maintenance.getTotal_value());
            statement.setInt(5, maintenance.getTicket_id());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    maintenance.setId(generatedKeys.getInt(1));
                }
            }
        }
        return maintenance;
    }

}
