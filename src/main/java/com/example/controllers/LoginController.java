package com.example.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController extends DatabaseAccess {
    String generateTempAuthToken() {
        return "boggart";
    }

    @PostMapping
    String login(@RequestParam String user_name,
                 @RequestParam String password_hash) {
        try (Connection connection = dataSource.getConnection()) {
            JSONObject res = new JSONObject();

            connection.setAutoCommit(false);

            ResultSet rs = this.prepareStatement(
                    "SELECT * " +
                            "FROM __DBNAME__.users " +
                            "WHERE user_name = ?",
                    connection,
                    user_name
            ).executeQuery();

            List<String> passwordHashList = new ArrayList<>();
            while (rs.next()) {
                passwordHashList.add(rs.getString("password_hash"));
            }
            boolean success = passwordHashList.contains(password_hash);
            res.put("success", success);



            if (success) {
                String newTempAuthToken = this.generateTempAuthToken();
                this.prepareStatement(
                        "UPDATE __DBNAME__.users " +
                                "SET temp_auth_token = ? " +
                                "WHERE user_name = ?",
                        connection,
                        newTempAuthToken,
                        user_name
                ).executeUpdate();
                res.put("temp_auth_token", newTempAuthToken);
            }

            connection.commit();

            return res.toString();
        } catch (Exception e) {
            return this.errorJSON(e).toString();
        }
    }
}
