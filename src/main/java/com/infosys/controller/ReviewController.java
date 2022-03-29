package com.infosys.controller;

import com.infosys.model.*;
import com.infosys.model.projection.ReviewView;
import com.infosys.service.*;
import com.infosys.service.DTO.FullReview;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FullReviewService fullReviewService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageService imageService;


    @GetMapping("/reviews/{id}")
    ReviewView getReviewView(@PathVariable Integer id) {
        return reviewService.getReviewView(id);
    }

    @GetMapping("/reviews/username")
    String getFullReviewsByUsernames(@RequestParam String[] usernames) {
        for (String str : usernames) {
            System.out.println(str);
        }
        return fullReviewService.getFullReviewsByUsernames(usernames).toString();
    }

    @GetMapping("/reviews/tags")
    String getFullReviewsByTagNames(@RequestParam String[] tagNames) {
        for (String str : tagNames) {
            System.out.println(str);
        }
        return fullReviewService.getFullReviewsByTagNames(tagNames).toString();
    }

    @PostMapping("/reviews")
    Review postReview(
            @RequestParam Integer userId,

            @RequestParam Integer productId,
            @RequestParam String productName,

            @RequestParam Integer vendorId,
            @RequestParam String vendorName,
            @RequestParam String vendorLocation,
            @RequestParam Long vendorPhoneNo,

            @RequestParam String[] imagesData,
            @RequestParam String[] tagNames,
            @RequestParam Integer rating,
            @RequestParam Integer unitsPurchased,
            @RequestParam String unit,
            @RequestParam BigDecimal pricePerUnit,
            @RequestParam String comments
    ) {
        Product product;
        if (productId == null) {
            if (productName.isEmpty()) {
                System.out.println("empty product id AND name");
                return null;
            }
            product = productService.save(new Product(productName));
        } else {
            product = productService.getById(productId);
            if (product == null) {
                System.out.println("product with that id does not exist");
                return null;
            }
        }

        Vendor vendor;
        if (vendorId == null) {
            if (vendorName.isEmpty()) {
                System.out.println("empty product id AND name");
                return null;
            }
            vendor = vendorService.save(new Vendor(vendorName, vendorLocation, vendorPhoneNo));
        } else {
            vendor = vendorService.getById(vendorId);
            if (vendor == null) {
                System.out.println("vendor with that id does not exist");
                return null;
            }
        }

        Review review = reviewService.save(new Review(
                userService.getById(userId),
                product,
                vendor,
                rating,
                unitsPurchased,
                unit,
                pricePerUnit,
                comments
        ));

        Set<Tag> tags = tagService.updateTagsAndLinkToReview(tagNames, review);

        //Set<Image> images = imageService.saveImagesAndLinkToReview(imagesData, review.getId());
        return review;
    }
}
