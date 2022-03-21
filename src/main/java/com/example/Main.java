/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

@RestController
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @PostMapping("/makeMessage")
  String makeTick(@RequestParam String body) {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      String updateString = "INSERT INTO test.messages VALUES (DEFAULT, now(), ?)";
      PreparedStatement stmt = connection.prepareStatement(updateString);
      stmt.setString(1, body);

      stmt.execute();

      connection.commit();

      return "set:" + body;
    } catch (Exception e) {
      return "error" + e.getMessage();
    }
  }

  @GetMapping("/readMessagesStartingWithLetter")
  String doesMessageExist(@RequestParam String startingLetter) {
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("into function now");
      if (startingLetter.length() != 1) return "Must be 1 letter!";

      connection.setAutoCommit(false);
      String updateString = "SELECT * FROM test.messages WHERE msg LIKE ?";
      PreparedStatement stmt = connection.prepareStatement(updateString);
      stmt.setString(1, startingLetter + "%");

      ResultSet rs = stmt.executeQuery();

      String output = "";
      while(rs.next()) {
        output += "We found: " + rs.getString("msg") + " at " + rs.getTimestamp("created_time") + "\n";
        System.out.println(output);
      }

      connection.commit();

      return "gotten:\n" + output;
    } catch (Exception e) {
      return "error" + e.getMessage();
    }
  }


  // @RequestMapping("/db")
  // String db(Map<String, Object> model) {
  //   try (Connection connection = dataSource.getConnection()) {
  //     Statement stmt = connection.createStatement();
  //     stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
  //     stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
  //     ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

  //     ArrayList<String> output = new ArrayList<String>();
  //     while (rs.next()) {
  //       output.add("Read from DB: " + rs.getTimestamp("tick"));
  //     }

  //     model.put("records", output);
  //     return "db";
  //   } catch (Exception e) {
  //     model.put("message", e.getMessage());
  //     return "error";
  //   }
  // }

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
