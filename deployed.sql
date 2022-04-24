DROP SCHEMA deployed CASCADE;

CREATE EXTENSION IF NOT EXISTS pg_trim;

CREATE SCHEMA deployed;

CREATE TABLE deployed.users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    temp_auth_token TEXT
);

CREATE TABLE deployed.vendors (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    location TEXT NOT NULL,
    phone_no BIGINT NOT NULL
);

CREATE TABLE deployed.products (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE deployed.reviews (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_ref INT REFERENCES deployed.users(id), -- nullable for possible anonymity
    product_ref INT REFERENCES deployed.products(id) ON DELETE CASCADE NOT NULL,
    vendor_ref INT REFERENCES deployed.vendors(id) ON DELETE CASCADE NOT NULL,
    rating INT NOT NULL,
    units_purchased INT NOT NULL,
    unit TEXT NOT NULL,
    price_per_unit DECIMAL(19, 4) NOT NULL,
    comments TEXT
);

CREATE TABLE deployed.images (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    data TEXT NOT NULL,
    review_ref INT REFERENCES deployed.reviews(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE deployed.tags (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE deployed.tags_to_reviews (
    tag_ref INT REFERENCES deployed.tags(id) ON DELETE CASCADE NOT NULL,
    review_ref INT REFERENCES deployed.reviews(id) ON DELETE CASCADE NOT NULL,

    PRIMARY KEY(review_ref, tag_ref)
);
