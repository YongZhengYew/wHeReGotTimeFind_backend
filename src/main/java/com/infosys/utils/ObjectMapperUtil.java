package com.infosys.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ObjectMapperUtil extends ObjectMapper {
    public <T> JSONObject getJSONObject(T thing) {
        JSONObject result;
        try {
            result = new JSONObject(writeValueAsString(thing));
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public ObjectMapperUtil copy() {
        return new ObjectMapperUtil();
    }
}
