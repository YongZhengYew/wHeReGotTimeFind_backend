package com.infosys.repository;

import com.infosys.model.Vendor;
import com.infosys.model.projection.VendorView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    VendorView findVendorViewById(Integer id);
}
