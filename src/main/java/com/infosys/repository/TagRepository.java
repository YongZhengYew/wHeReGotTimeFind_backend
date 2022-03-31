package com.infosys.repository;

import com.infosys.model.Tag;
import com.infosys.model.projection.TagView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByName(String tagName);

    @Query(value = "SELECT * FROM test.tags t WHERE SIMILARITY(name, ?1) > 0.1;", nativeQuery = true)
    Set<TagView> findTagViewsByNameFuzzy(String tagName);
}
