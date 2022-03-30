package com.infosys.service;

import com.infosys.model.Image;
import com.infosys.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService extends GenericModelService<Image, Integer, ImageRepository> {
}
