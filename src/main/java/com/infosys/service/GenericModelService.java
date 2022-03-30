package com.infosys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class GenericModelService<T,K,V extends JpaRepository<T,K>> {
    @Autowired
    protected V repository;

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
