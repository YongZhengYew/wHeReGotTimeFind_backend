package com.infosys.repository;

import com.infosys.model.Image;
import com.infosys.model.projection.ImageView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Set<ImageView> findByReviewId(Integer reviewId);
}
