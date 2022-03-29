package com.infosys.service;

import com.infosys.model.Review;
import com.infosys.model.Tag;
import com.infosys.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService extends GenericModelService<Tag, Integer, TagRepository> {
    public Set<Tag> updateTagsAndLinkToReview(String[] tagNames, Review review) {
        Set<Tag> result = new HashSet<>();
        for (String tagName : tagNames) {
            Optional<Tag> optTag = repository.findByName(tagName);
            if (optTag.isPresent()) {
                Tag tag = optTag.get();
                tag.updateTaggedReviews(review);
                result.add(repository.save(tag));
            } else {
                Tag newTag = new Tag(tagName);
                newTag.updateTaggedReviews(review);
                result.add(repository.save(newTag));
            }
        }
        return result;
    }
}
