package com.loca_mais.backend.exceptions.util;

import java.sql.SQLException;

public class PostgresError {

    public static boolean isDuplicateKeyError(SQLException ex) {
        return "23505".equals(ex.getSQLState());
    }
}
