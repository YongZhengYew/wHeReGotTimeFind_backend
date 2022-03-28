package com.example.controllers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseAccess {

    @Value("${spring.datasource.url}")
    protected String dbUrl;

    protected final String dbName = "test";

    @Autowired
    protected DataSource dataSource;


    String insertDBName(String sqlString) {
        return sqlString.replaceAll("__DBNAME__", this.dbName);
    }

    JSONObject errorJSON(Exception e) {
        JSONObject res = new JSONObject();
        res.put("ERROR", e.getMessage());
        return res;
    }

    JSONObject gatherObject(ResultSet rs, String ... properties) throws SQLException {
        JSONObject entry = new JSONObject();
        for (String str : properties) {
            entry.put(str, rs.getObject(str));
        }
        return entry;
    }


    PreparedStatement prepareStatement(String sqlStatement, Connection connection, Object[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(this.insertDBName(sqlStatement));
        int i = 1;
        for (Object o : params) {
            preparedStatement.setObject(i++, o);
        }
        return preparedStatement;
    }

    List<Object> getListFromSingleColumn(String sqlQuery, Connection connection,  Object[] params, String columnName) throws SQLException {
        ResultSet rs = this.prepareStatement(sqlQuery, connection, params).executeQuery();
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getObject(columnName));
        }
        return result;
    }

    boolean validateRequestCredentials(String user_name, String temp_auth_token) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            ResultSet rs = this.prepareStatement(
                    "SELECT temp_auth_token " +
                            "FROM __DBNAME__.users " +
                            "WHERE user_name = ?",
                    connection,
                    new Object[] {user_name}
            ).executeQuery();

            boolean success = false;
            while (rs.next()) {
                if (rs.getString("temp_auth_token").equals(temp_auth_token)) {
                    success = true;
                    break;
                }
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }
    }
}
