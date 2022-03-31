package com.infosys.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.utils.ObjectMapperUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.PreparedStatementSetter;

public abstract class GenericModelService<T,K,V extends JpaRepository<T,K>> {
    @Autowired
    protected V repository;

    @Autowired
    protected ObjectMapperUtil objectMapperUtil;

    public T getById(K id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }

    public T save(T thing) {
        return repository.save(thing);
    }
}
