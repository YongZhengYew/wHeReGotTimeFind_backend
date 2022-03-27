package com.example.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

@RestController
@RequestMapping("/reviews")
public class ReviewController extends DatabaseAccess{

    @GetMapping("/tag/{tagName}")
    String getReviewByTagName(@PathVariable String tagName) {
        try (Connection connection = this.dataSource.getConnection()) {
            ResultSet rs = this.prepareStatement(
                    "SELECT "+
                    "    r.*, "+
                    "    v.* "+
                    "FROM "+
                    "    __DBNAME__.reviews r "+
                    "        JOIN __DBNAME__.tags_to_reviews t_r ON r.id = t_r.review_ref "+
                    "        JOIN __DBNAME__.tags t ON t.id = t_r.tag_ref, "+
                    "    __DBNAME__.vendors v "+
                    "WHERE "+
                    "    t.name = ? "+
                    "    AND v.id = r.vendor_ref ",
                    connection,
                    tagName
            ).executeQuery();

            JSONObject result = new JSONObject();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberofColumns = rsmd.getColumnCount();

            int count = 0;
            while (rs.next()) {
                JSONObject entry = new JSONObject();
                for (int i = 1; i <= numberofColumns; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    entry.put(rsmd.getColumnName(i), columnValue);
                }
                result.put(String.valueOf(count), entry);
                count++;
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorJSON(e).toString();
        }
    }

    @GetMapping("/username/{username}")
    String getReviewByUsername(@PathVariable String username) {
        try (Connection connection = this.dataSource.getConnection()) {
            ResultSet rs = this.prepareStatement(
                    "SELECT "+
                    "    r.*, "+
                    "    u.* "+
                    "FROM "+
                    "    __DBNAME__.reviews r, "+
                    "    __DBNAME__.users u "+
                    "WHERE "+
                    "    r.user_ref = u.id "+
                    "    AND u.name = ? ",
                    connection,
                    username
            ).executeQuery();

            JSONObject result = new JSONObject();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberofColumns = rsmd.getColumnCount();

            int count = 0;
            while (rs.next()) {
                JSONObject entry = new JSONObject();
                for (int i = 1; i <= numberofColumns; i++) {
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    entry.put(rsmd.getColumnName(i), columnValue);
                }
                result.put(String.valueOf(count), entry);
                count++;
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorJSON(e).toString();
        }
    }

//    @PostMapping
//    void postReview(
//            @RequestParam Integer userID,
//            @RequestParam String productName,
//            @RequestParam String vendorName,
//            @RequestParam Integer starRating,
//            @RequestParam Integer unitsPurchased,
//            @RequestParam Integer unitSize,
//            @RequestParam Integer pricePerUnitInCents,
//            @RequestParam String bodyText
//    ) {
//        try (Connection connection = this.dataSource.getConnection()) {
//            ResultSet productID = this.prepareStatement(
//
//            ).executeQuery();
//
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

}
