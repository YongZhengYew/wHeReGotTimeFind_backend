package com.infosys.controller;

import com.infosys.model.*;
import com.infosys.model.projection.ReviewView;
import com.infosys.service.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("/reviews")
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

    @GetMapping("/{id}")
    ReviewView getReviewView(@PathVariable Integer id) {
        return reviewService.getReviewView(id);
    }

    @GetMapping("/username")
    String getFullReviewsByUsernames(@RequestParam String[] usernames) {
        for (String str : usernames) {
            System.out.println(str);
        }
        return fullReviewService.getFullReviewsByUsernames(usernames).toString();
    }

    @GetMapping("/tags")
    String getFullReviewsByTagIds(@RequestParam Integer[] tagIds) {
        return fullReviewService.getFullReviewsByTagIds(tagIds).toString();
    }

    @GetMapping("/productName/{productName}")
    String getFullReviewsByProductName(@PathVariable String productName) {
        return fullReviewService.getFullReviewsByProductNameFuzzy(productName).toString();
    }

    @GetMapping("/vendorName/{vendorName}")
    String getFullReviewsByVendorName(@PathVariable String vendorName) {
        return fullReviewService.getFullReviewsByVendorNameFuzzy(vendorName).toString();
    }

    @PostMapping
    Review postReview(
            @RequestParam Integer existingUserId,

            @RequestParam(required = false) Integer existingProductId,
            @RequestParam(required = false) String newProductName,

            @RequestParam(required = false) Integer existingVendorId,
            @RequestParam(required = false) String newVendorName,
            @RequestParam(required = false) String newVendorLocation,
            @RequestParam(required = false) Long newVendorPhoneNo,

            @RequestParam(required = false) String[] imagesData,

            @RequestParam(required = false) Integer[] existingTagIds,
            @RequestParam(required = false) String[] newTagNames,

            @RequestParam Integer rating,
            @RequestParam Integer unitsPurchased,
            @RequestParam String unit,
            @RequestParam BigDecimal pricePerUnit,
            @RequestParam(required = false) String comments
    ) {
        if (existingUserId == null) return null;
        if (existingProductId == null && newProductName.isEmpty())
            return null;
        if (existingVendorId == null && (
                newVendorName.isEmpty() ||
                newVendorLocation.isEmpty() ||
                newVendorPhoneNo == null
        ))
            return null;

        System.out.println("XXXXXXXXXX");

        User user = userService.getById(existingUserId);
        if (user == null) return null;

        System.out.println("XXXXXXXXXX");

        Product product = null;
        if (existingProductId != null) {
            Product getResult = productService.getById(existingProductId);
            if (getResult == null) return null;
            else product = getResult;
        }

        System.out.println("XXXXXXXXXX");

        Vendor vendor = null;
        if (existingVendorId != null) {
            Vendor getResult = vendorService.getById(existingVendorId);
            if (getResult == null) return null;
            else vendor = getResult;
        }

        System.out.println("XXXXXXXXXX");

        if (existingTagIds != null && newTagNames != null) {
            Stream<Tag> tagsGetResult = Arrays.stream(existingTagIds)
                    .map(tagService::getById);

            boolean tagIdsValid = tagsGetResult.noneMatch(Objects::isNull);
            if (!tagIdsValid) return null;

            boolean tagsNewOldClashCheck = tagsGetResult.noneMatch(tag ->
                    Arrays.stream(newTagNames).toList().contains(tag.getName())
            );
            if (!tagsNewOldClashCheck) return null;
        }

        System.out.println("XXXXXXXXXX");

        if (product == null)
            product = productService.save(new Product(newProductName));

        System.out.println("XXXXXXXXXX");
        if (vendor == null)
            vendor = vendorService.save(new Vendor(newVendorName, newVendorLocation, newVendorPhoneNo));
        System.out.println("XXXXXXXXXX");
        Review review = reviewService.save(new Review(
                user,
                product,
                vendor,
                rating,
                unitsPurchased,
                unit,
                pricePerUnit,
                comments
        ));
        System.out.println("XXXXXXXXXX");

        if (existingTagIds != null)
            tagService.linkExistingTags(existingTagIds, review);
        System.out.println("XXXXXXXXXX");
        if (newTagNames != null)
            tagService.makeNewTagsAndLink(newTagNames, review);
        System.out.println("XXXXXXXXXX");

        if (imagesData != null)
            imageService.saveImagesAndLinkToReview(imagesData, review);
        System.out.println("XXXXXXXXXX");

        return review;
    }
}
