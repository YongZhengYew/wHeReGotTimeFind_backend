package com.infosys.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags", schema = "test")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @ManyToMany
    @JoinTable(
            name = "tags_to_reviews", schema = "test",
            joinColumns = @JoinColumn(name = "tag_ref"),
            inverseJoinColumns = @JoinColumn(name = "review_ref"))
    private Set<Review> taggedReviews;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Review> getTaggedReviews() {
        return taggedReviews;
    }

    public void setTaggedReviews(Set<Review> taggedReviews) {
        this.taggedReviews = taggedReviews;
    }

    public void updateTaggedReviews(Review taggedReview) {
        this.taggedReviews.add(taggedReview);
    }

    public Tag() {}

    public Tag(String name) {
        this.name = name;
        this.taggedReviews = new HashSet<>();
    }
}
