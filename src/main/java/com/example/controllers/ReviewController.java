package com.example.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reviews")
public class ReviewController extends DatabaseAccess{

    final String reviewSpecSelectStatement =
            "SELECT "+
            "    r.id AS reviewid, " +
            "    r.user_ref AS userid, "+
            "    r.rating, "+
            "    r.units_purchased, "+
            "    r.unit, "+
            "    r.price_per_unit, "+
            "    r.comments, "+
            "    v.name, "+
            "    v.location, "+
            "    v.phone_no, "+
            "    p.name AS product_name ";

    @GetMapping("/tag/{tagName}")
    String getReviewsByTagName(@PathVariable String tagName) {
        try (Connection connection = this.dataSource.getConnection()) {
            ResultSet rs = this.prepareStatement(
                    this.reviewSpecSelectStatement +
                    "FROM "+
                    "    test.reviews r "+
                    "        JOIN test.tags_to_reviews t_r ON r.id = t_r.review_ref "+
                    "        JOIN test.tags t ON t.id = t_r.tag_ref, "+
                    "    test.vendors v, "+
                    "    test.products p "+
                    "WHERE "+
                    "    v.id = r.vendor_ref "+
                    "    AND p.id = r.product_ref "+
                    "    AND t.name = ? ",
                    connection,
                    new Object[] {tagName}
            ).executeQuery();

            JSONObject result = this.getReviewsToSpecFromResultSet(rs, connection);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorJSON(e).toString();
        }
    }

    @GetMapping("/username/{username}")
    String getReviewsByUsername(@PathVariable String username) {
        try (Connection connection = this.dataSource.getConnection()) {
            ResultSet rs = this.prepareStatement(
                    this.reviewSpecSelectStatement +
                    "FROM "+
                    "    test.reviews r, "+
                    "    test.vendors v, "+
                    "    test.products p, "+
                    "    test.users u "+
                    "WHERE "+
                    "    v.id = r.vendor_ref "+
                    "    AND p.id = r.product_ref "+
                    "    AND r.user_ref = u.id "+
                    "    AND u.name = ? ",
                    connection,
                    new Object[] {username}
            ).executeQuery();

            JSONObject result = this.getReviewsToSpecFromResultSet(rs, connection);
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

    JSONObject getReviewsToSpecFromResultSet(ResultSet rs, Connection connection) throws SQLException {
        JSONArray fullReviewArray = new JSONArray();
        while (rs.next()) {

            JSONObject vendor = this.gatherObject(
                    rs,
                    "name",
                    "location",
                    "phone_no"
            );
            JSONObject review = this.gatherObject(
                    rs,
                    "userid",
                    "rating",
                    "units_purchased",
                    "unit",
                    "price_per_unit",
                    "comments",
                    "product_name"
            );

            List<Object> tagNamesPerReview = this.getListFromSingleColumn(
                    "SELECT "+
                            "    t.name "+
                            "FROM "+
                            "    test.reviews r "+
                            "        JOIN test.tags_to_reviews tr ON r.id = tr.review_ref, "+
                            "    test.tags t "+
                            "WHERE "+
                            "    t.id = tr.tag_ref "+
                            "    AND r.id = ? ",
                    connection,
                    new Object[] {rs.getObject("reviewid")},
                    "name"
            );
            review.put("tags", tagNamesPerReview);

            List<Object> imagesPerReview = this.getListFromSingleColumn(
                    "SELECT "+
                            "    i.data "+
                            "FROM "+
                            "    test.reviews r "+
                            "        JOIN test.images_to_reviews ir ON r.id = ir.review_ref, "+
                            "    test.images i "+
                            "WHERE "+
                            "    i.id = ir.image_ref "+
                            "    AND r.id = ? ",
                    connection,
                    new Object[] {rs.getObject("reviewid")},
                    "data"
            );
            review.put("images", imagesPerReview);

            JSONObject fullReview = new JSONObject();
            fullReview.put("vendor", vendor);
            fullReview.put("review", review);
            fullReviewArray.put(fullReview);
        }
        JSONObject result = new JSONObject();
        result.put("full_reviews", fullReviewArray);
        return result;
    }

}
