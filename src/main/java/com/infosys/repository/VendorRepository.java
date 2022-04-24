package com.infosys.repository;

import com.infosys.model.Vendor;
import com.infosys.model.projection.VendorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    VendorView findVendorViewById(Integer id);

    // must name phone_no -> phoneNo or else you will get mysterious null values
    @Query(value = "SELECT v.name as name, v.id as id, v.phone_no as phoneNo, v.location as location FROM deployed.vendors v WHERE SIMILARITY(name, ?1) > 0.1;", nativeQuery = true)
    List<VendorView> findVendorViewByNameFuzzy(String name);
}
