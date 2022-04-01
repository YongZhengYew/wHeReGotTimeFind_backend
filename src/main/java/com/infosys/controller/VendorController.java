package com.infosys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.model.Vendor;
import com.infosys.model.projection.VendorView;
import com.infosys.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // Save operation
    @PostMapping
    public Vendor saveVendor(@Valid @RequestBody Vendor vendor) {
            return vendorService.save(vendor);
    }

    // Read operation
    @GetMapping("/{id}")
    public VendorView getVendorbyId(@PathVariable Integer id)
    {
        return vendorService.getVendorViewById(id);
    }

    @GetMapping("/vendorName/{vendorName}")
    public String getVendorByNameFuzzy(@PathVariable String vendorName) {
        return vendorService.getByNameFuzzy(vendorName).toString();
    }

    // Delete operation
    @DeleteMapping("/{id}")
    public String deleteDepartmentById(@PathVariable("id") Integer id) {
        vendorService.deleteById(id);
        return "Deleted Successfully";
    }
}
