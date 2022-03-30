package com.infosys.service;

import com.infosys.model.Vendor;
import com.infosys.repository.VendorRepository;
import org.springframework.stereotype.Service;

@Service
public class VendorService extends GenericModelService<Vendor, Integer, VendorRepository> {
}
