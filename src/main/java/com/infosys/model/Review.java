package com.infosys.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "reviews", schema = "test")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference(value = "user-review")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_ref", nullable = false)
    private User user;

    @JsonBackReference(value = "product-review")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_ref", nullable = false)
    private Product product;

    @JsonBackReference(value = "vendor-review")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_ref", nullable = false)
    private Vendor vendor;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
    @ManyToMany(mappedBy = "taggedReviews", fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @JsonManagedReference(value = "review-image")
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Image> images;

    private Integer rating;
    @Column(name = "units_purchased")
    private Integer unitsPurchased;
    private String unit;
    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;
    private String comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getUnitsPurchased() {
        return unitsPurchased;
    }

    public void setUnitsPurchased(Integer unitsPurchased) {
        this.unitsPurchased = unitsPurchased;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Review() {}

    public Review(User user,
                  Product product,
                  Vendor vendor,
                  Integer rating,
                  Integer unitsPurchased,
                  String unit,
                  BigDecimal pricePerUnit,
                  String comments
    ) {
        this.user = user;
        this.product = product;
        this.vendor = vendor;
        this.rating = rating;
        this.unitsPurchased = unitsPurchased;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.comments = comments;
    }
}
