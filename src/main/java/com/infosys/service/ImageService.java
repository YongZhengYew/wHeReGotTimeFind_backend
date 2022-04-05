package com.infosys.service;

import com.infosys.model.Image;
import com.infosys.model.Review;
import com.infosys.model.projection.ImageView;
import com.infosys.repository.ImageRepository;
import com.infosys.repository.ReviewRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ImageService extends GenericModelService<Image, Integer, ImageRepository> {
    @Autowired
    private ReviewRepository reviewRepository;

    public JSONArray saveImagesAndLinkToReview(String[] imagesData, Review review) {
        JSONArray result = new JSONArray();
        Arrays.stream(imagesData)
                .map(imageData -> new Image(imageData, review))
                .map(repository::save)
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
//        for (String imageData : imagesData) {
//            Image image = repository.save(new Image(imageData, review));
//            result.put(objectMapperUtil.getJSONObject(image));
//        }
        return result;
    }

    public JSONArray getImagesByReviewId(Integer reviewId) {
        JSONArray result = new JSONArray();
        for (ImageView image : repository.findByReviewId(reviewId)) {
            result.put(objectMapperUtil.getJSONObject(image));
        }
        return result;
    }
}
