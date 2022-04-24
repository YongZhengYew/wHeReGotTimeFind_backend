package com.infosys.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="users", schema = "deployed")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "temp_auth_token")
    private String tempAuthToken;

    @JsonManagedReference(value = "user-review")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Review> reviews;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getTempAuthToken() {
        return tempAuthToken;
    }

    public void setTempAuthToken(String tempAuthToken) {
        this.tempAuthToken = tempAuthToken;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public User() {}

    public User(String name, String passwordHash, String tempAuthToken) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.tempAuthToken = tempAuthToken;
    }
}
