package com.infosys.controller;

import com.infosys.model.*;
import com.infosys.model.projection.ReviewView;
import com.infosys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
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
        return fullReviewService.getFullReviewsByUsernames(usernames).toString();
    }

    @DeleteMapping("/{id}")
    String deleteReview(
            @RequestParam(required = false) String username,
            @PathVariable Integer id,
            @RequestParam(required = false) boolean debug
    ) {
        Review review = reviewService.getById(id);

        if (review == null) return null;

        if (!debug) {
            if (username == null) return null;
            if (!review.getUser().getName().equals(username)) return "you aren't the owner";
        }

        reviewService.deleteById(id);
        return "delete successful";
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

    @PostMapping(value = "addImage")
    String addImageToReview(
            @RequestParam("imageData") MultipartFile imageData,
            @RequestParam Integer reviewId
    ) {
        try {
            String base64String = new String(imageData.getBytes(), StandardCharsets.UTF_8);
            Review review = reviewService.getById(reviewId);
            if (review == null) return null;

            imageService.save(new Image(base64String, review));
            return review.getImages().toString();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping
    Review postReview(
            // User
            @RequestParam Integer existingUserId,

            // Product
            @RequestParam(required = false) Integer existingProductId,
            @RequestParam(required = false) String newProductName,

            // Vendor
            @RequestParam(required = false) Integer existingVendorId,
            @RequestParam(required = false) String newVendorName,
            @RequestParam(required = false) String newVendorLocation,
            @RequestParam(required = false) Long newVendorPhoneNo,

            // Images
            @RequestParam(required = false) String[] imagesData,

            // Tags
            @RequestParam(required = false) Integer[] existingTagIds,
            @RequestParam(required = false) String[] newTagNames,

            // Misc
            @RequestParam Integer rating,
            @RequestParam Integer unitsPurchased,
            @RequestParam String unit,
            @RequestParam BigDecimal pricePerUnit,
            @RequestParam(required = false) String comments
    ) {
        /*
        Validation of endpoint parameters. All possible combinations of parameters
        (optional and compulsory) must be validated before any modification of the
        database can begin, which makes the code a bit long-winded.
        */

        // Must have user id. No anonymous reviews in our implementation
        if (existingUserId == null) return null;

        // Every review must be linked to a product. Either an existing product
        // must be indicated with an id, or a new product name must be provided
        if (existingProductId == null && newProductName.isEmpty())
            return null;

        // Similarly, either an existing vendor must be indicated with an id,
        // or new vendor details must be provided
        if (
                existingVendorId == null && (
                    newVendorName.isEmpty() ||
                    newVendorLocation.isEmpty() ||
                    newVendorPhoneNo == null
                )
        )
            return null;

        // Check if provided user id is valid
        User user = userService.getById(existingUserId);
        if (user == null) return null;

        // If provided with existing product id, then check validity
        Product product = null;
        if (existingProductId != null) {
            Product getResult = productService.getById(existingProductId);
            if (getResult == null) return null;
            else product = getResult;
        }

        // If provided with existing vendor id, then check validity
        Vendor vendor = null;
        if (existingVendorId != null) {
            Vendor getResult = vendorService.getById(existingVendorId);
            if (getResult == null) return null;
            else vendor = getResult;
        }

        // Tags are not yet implemented on the app (always null received), but we have a handler for future
        // If provided with existing tag ids, then check validity
        if (existingTagIds != null && newTagNames != null) {
            Supplier<Stream<Tag>> tagsGetResult = () -> Arrays.stream(existingTagIds)
                    .map(tagService::getById);

            boolean tagIdsValid = tagsGetResult.get().noneMatch(Objects::isNull);
            if (!tagIdsValid) return null;

            // Unlike for products and vendors, we can have both new tags and old tags in one request
            // (since more than one tag can be added to a review). Thus, we must check that new tag
            // names do not clash with existing tag names
            boolean tagsNewOldClashCheck = tagsGetResult.get().noneMatch(tag ->
                    Arrays.stream(newTagNames).toList().contains(tag.getName())
            );
            if (!tagsNewOldClashCheck) return null;
        }

        /* All parameters validated, start calling service operations to modify databases */

        if (product == null)
            product = productService.save(new Product(newProductName));

        if (vendor == null)
            vendor = vendorService.save(new Vendor(newVendorName, newVendorLocation, newVendorPhoneNo));

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

        if (existingTagIds != null)
            tagService.linkExistingTags(existingTagIds, review);

        if (newTagNames != null)
            tagService.makeNewTagsAndLink(newTagNames, review);

        if (imagesData != null)
            imageService.saveImagesAndLinkToReview(imagesData, review);

        return review;
    }
}
