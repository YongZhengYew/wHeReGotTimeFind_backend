package com.infosys.repository;

import com.infosys.model.Product;
import com.infosys.model.projection.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM deployed.products p WHERE SIMILARITY(name, ?1) > 0.1;", nativeQuery = true)
    List<ProductView> findByNameFuzzy(String productNameFuzzy);

    Set<ProductView> findDistinctByReviewsVendorId(Integer vendorId);
}
