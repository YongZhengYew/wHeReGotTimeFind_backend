package com.infosys.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ReviewView {
    Integer getId();

    Integer getRating();
    Integer getUnitsPurchased();
    String getUnit();
    BigDecimal getPricePerUnit();
    String getComments();

    UserView getUser();
    ProductView getProduct();
    VendorView getVendor();

    List<TagView> getTags();
    List<ImageView> getImages();
}
