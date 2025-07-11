package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.PaymentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
@AllArgsConstructor
public class PaymentDAO {

    private final DataSource  dataSource;


    public void save(PaymentEntity payment) {
        String sql = "INSERT INTO payments (value,tax,contract_id) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, payment.getValue());
            stmt.setDouble(2, payment.getTax());
            stmt.setInt(3, payment.getContractId());


            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao salvar pagamento, nenhuma linha afetada.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar pagamento: " + e.getMessage(), e);
        }
    }


}
