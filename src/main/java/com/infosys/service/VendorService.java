package com.infosys.service;

import com.infosys.model.Vendor;
import com.infosys.model.projection.ReviewView;
import com.infosys.model.projection.VendorView;
import com.infosys.repository.VendorRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class VendorService extends GenericModelService<Vendor, Integer, VendorRepository> {
    public JSONArray getByNameFuzzy(String vendorName) {
        JSONArray result = new JSONArray();
        

        repository.findVendorViewByNameFuzzy(vendorName).stream()
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }

    public VendorView getVendorViewById(Integer id) {
        return repository.findVendorViewById(id);
    }
}
