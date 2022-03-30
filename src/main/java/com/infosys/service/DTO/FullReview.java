package com.infosys.service.DTO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.infosys.model.projection.ImageView;
import com.infosys.model.projection.ReviewView;
import com.infosys.model.projection.TagView;
import com.infosys.model.projection.VendorView;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FullReview {
    private ReviewStub review;
    private VendorView vendor;

    static class ReviewStub {
        private Integer userid;
        private String product_name;
        private Integer rating;
        private Integer units_purchased;
        private String unit;

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public Integer getUnits_purchased() {
            return units_purchased;
        }

        public void setUnits_purchased(Integer units_purchased) {
            this.units_purchased = units_purchased;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getPrice_per_unit() {
            return price_per_unit;
        }

        public void setPrice_per_unit(BigDecimal price_per_unit) {
            this.price_per_unit = price_per_unit;
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

        private BigDecimal price_per_unit;
        private List<String> tags;
        private List<String> images;
        private String comments;

        public ReviewStub(
                Integer userid,
                String product_name,
                Integer rating,
                Integer units_purchased,
                String unit,
                BigDecimal price_per_unit,
                List<String> tags,
                List<String> images,
                String comments
        ) {
            this.userid = userid;
            this.product_name = product_name;
            this.rating = rating;
            this.units_purchased = units_purchased;
            this.unit = unit;
            this.price_per_unit = price_per_unit;
            this.tags = tags;
            this.images = images;
            this.comments = comments;
        }
    }

    public FullReview(ReviewView reviewView) {
        this.vendor = reviewView.getVendor();
        this.review = new ReviewStub(
                reviewView.getUser().getId(),
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

//    public ReviewStub getReviewStub() {
//        return review;
//    }
//
//    public void setReviewStub(ReviewStub reviewStub) {
//        this.review = reviewStub;
//    }
//
//    public VendorView getVendorView() {
//        return vendor;
//    }
//
//    public void setVendorView(VendorView vendorView) {
//        this.vendor = vendorView;
//    }
}
