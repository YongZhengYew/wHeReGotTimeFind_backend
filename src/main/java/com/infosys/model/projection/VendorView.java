package com.infosys.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface VendorView {
    Integer getId();
    String getName();
    String getLocation();
    Long getPhoneNo();
}
