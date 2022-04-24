package com.infosys.service.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.infosys.model.projection.ImageView;
import com.infosys.model.projection.ReviewView;
import com.infosys.model.projection.TagView;
import com.infosys.model.projection.VendorView;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FullReview {
    private ReviewStub review;
    private VendorView vendor;

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ReviewStub {
        private Integer reviewid;
        private Integer userid;
        private String username;
        private String productName;
        private Integer rating;
        private Integer unitsPurchased;
        private String unit;

        public Integer getReviewid() {
            return reviewid;
        }

        public void setReviewid(Integer reviewid) {
            this.reviewid = reviewid;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public Integer getUnitsPurchased() {
            return unitsPurchased;
        }

        public void setUnitsPurchased(Integer unitsPurchased) {
            this.unitsPurchased = unitsPurchased;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getPricePerUnit() {
            return pricePerUnit;
        }

        public void setPricePerUnit(BigDecimal pricePerUnit) {
            this.pricePerUnit = pricePerUnit;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        private BigDecimal pricePerUnit;
        private List<String> tags;
        private List<String> images;
        private String comments;

        public ReviewStub(
                Integer reviewid,
                Integer userid,
                String username,
                String product_name,
                Integer rating,
                Integer units_purchased,
                String unit,
                BigDecimal price_per_unit,
                List<String> tags,
                List<String> images,
                String comments
        ) {
            this.reviewid = reviewid;
            this.userid = userid;
            this.username = username;
            this.productName = product_name;
            this.rating = rating;
            this.unitsPurchased = units_purchased;
            this.unit = unit;
            this.pricePerUnit = price_per_unit;
            this.tags = tags;
            this.images = images;
            this.comments = comments;
        }
    }

    public FullReview(ReviewView reviewView) {
        this.vendor = reviewView.getVendor();
        this.review = new ReviewStub(
                reviewView.getId(),
                reviewView.getUser().getId(),
                reviewView.getUser().getName(),
                reviewView.getProduct().getName(),
                reviewView.getRating(),
                reviewView.getUnitsPurchased(),
                reviewView.getUnit(),
                reviewView.getPricePerUnit(),
                reviewView.getTags().stream().map(TagView::getName).collect(Collectors.toList()),
                reviewView.getImages().stream().map(ImageView::getData).collect(Collectors.toList()),
                reviewView.getComments()
        );
    }
}
