package com.infosys.controller;

import com.infosys.model.*;
import com.infosys.model.projection.ReviewView;
import com.infosys.service.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
        for (String str : usernames) {
            System.out.println(str);
        }
        return fullReviewService.getFullReviewsByUsernames(usernames).toString();
    }

    @DeleteMapping("/{id}")
    String deleteReview(@RequestParam String username, @PathVariable Integer id, @RequestParam(required = false) boolean debug) {
        Review review = reviewService.getById(id);

        if (review == null) return null;

        if (!debug) {
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

    @PostMapping("test")
    String test(
            @RequestParam String text,
            @RequestParam Integer number,
            @RequestParam MultipartFile file
    ) {
        try {
            return "RECEIVED: \n" +
                    text + "\n" +
                    number + "\n" +
                    new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "huh?";
        }
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

            @RequestParam(required = false) MultipartFile[] imagesData,

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
            Supplier<Stream<Tag>> tagsGetResult = () -> Arrays.stream(existingTagIds)
                    .map(tagService::getById);

            boolean tagIdsValid = tagsGetResult.get().noneMatch(Objects::isNull);
            if (!tagIdsValid) return null;

            boolean tagsNewOldClashCheck = tagsGetResult.get().noneMatch(tag ->
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

        if (imagesData != null) {
            System.out.println("YYY");
            List<String> images = new ArrayList<>();
            System.out.println("YYY");
            for (MultipartFile image : imagesData) {
                System.out.println("ZZZ");
                try {
                    images.add(new String(image.getBytes(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    System.out.println("huh?");
                }
            }
            System.out.println("YYY");
            String[] arr = images.toArray(String[]::new);
            System.out.println("YYY");
            System.out.println("HEYO: " + arr[0]);
            imageService.saveImagesAndLinkToReview(arr, review);
            System.out.println("YYY");
        }

        System.out.println("XXXXXXXXXX");

        return review;
    }
}
