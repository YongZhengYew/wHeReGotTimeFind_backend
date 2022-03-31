package com.infosys.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.model.Review;
import com.infosys.model.Tag;
import com.infosys.model.projection.TagView;
import com.infosys.repository.TagRepository;
import io.micrometer.core.instrument.Tags;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService extends GenericModelService<Tag, Integer, TagRepository> {

    public JSONArray linkExistingTags(Integer[] tagIds, Review review) {
        JSONArray result = new JSONArray();
        for (Integer tagId : tagIds) {
            Tag getResult = getById(tagId);
            if (getResult == null) return null;
            else {
                getResult.updateTaggedReviews(review);
                result.put(getResult);
            }
        }
        return result;
    }

    public JSONArray makeNewTagsAndLink(String[] tagNames, Review review) {
        JSONArray result = new JSONArray();
        Arrays.stream(tagNames)
                .map(Tag::new)
                .map(repository::save)
                .peek(tag -> tag.updateTaggedReviews(review))
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
        return result;
    }

//    public Set<Tag> updateTagsAndLinkToReview(String[] tagNames, Review review) {
//        Set<Tag> result = new HashSet<>();
//        for (String tagName : tagNames) {
//            Optional<Tag> optTag = repository.findByName(tagName);
//            if (optTag.isPresent()) {
//                Tag tag = optTag.get();
//                tag.updateTaggedReviews(review);
//                result.add(repository.save(tag));
//            } else {
//                Tag newTag = new Tag(tagName);
//                newTag.updateTaggedReviews(review);
//                result.add(repository.save(newTag));
//            }
//        }
//        return result;
//    }

    public JSONArray getByNameList(String tagNames) {
        JSONArray result = new JSONArray();
        repository.findTagViewsByNameFuzzy(tagNames).stream()
                .map(objectMapperUtil::getJSONObject)
                .forEach(result::put);
//        Set<TagView> tags = repository.findTagViewsByNameFuzzy(tagNames);
//        for (TagView tag : tags) {
//            result.put(objectMapperUtil.getJSONObject(tag));
//        }
        return result;
    }
}