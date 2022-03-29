package com.infosys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.model.Vendor;
import com.infosys.model.projection.VendorView;
import com.infosys.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // Save operation
    @PostMapping("/vendors")
    public Vendor saveVendor(@Valid @RequestBody Vendor vendor) {
            return vendorService.save(vendor);
    }

    // Read operation
    @GetMapping("/vendors/{id}")
    public Vendor getVendorbyId(@PathVariable Integer id)
    {
        return vendorService.getById(id);
    }

    // Delete operation
    @DeleteMapping("/vendors/{id}")
    public String deleteDepartmentById(@PathVariable("id") Integer id) {
        vendorService.deleteById(id);
        return "Deleted Successfully";
    }
}
