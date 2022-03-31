package com.infosys.repository;

import com.infosys.model.Review;
import com.infosys.model.projection.ReviewView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    ReviewView findReviewViewById(Integer id);
    List<ReviewView> findReviewViewByUserName(String username);
    List<ReviewView> findReviewViewByProductId(Integer productId);
    List<ReviewView> findReviewViewByVendorId(Integer vendorId);
    List<ReviewView> findDistinctByTagsIdIn(Integer[] tagIds);

//    @Query(value = "",nativeQuery = true)
//    List<ReviewView> findDistinctByTagsIdAllMatching(Integer[] tagIds);


}
