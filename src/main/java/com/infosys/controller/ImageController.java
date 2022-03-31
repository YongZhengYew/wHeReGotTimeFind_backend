package com.infosys.controller;

import com.infosys.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/review/{reviewId}")
    private String getImageByReviewId(@PathVariable Integer reviewId) {
        return imageService.getImagesByReviewId(reviewId).toString();
    }
}
