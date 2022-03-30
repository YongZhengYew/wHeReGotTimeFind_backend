package com.infosys.model.projection;

import java.math.BigDecimal;
import java.util.List;

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
