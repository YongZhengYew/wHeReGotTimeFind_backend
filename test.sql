DROP SCHEMA test CASCADE;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE SCHEMA test;

CREATE TABLE test.users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    temp_auth_token TEXT
);

CREATE TABLE test.vendors (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    location TEXT NOT NULL,
    phone_no BIGINT NOT NULL
);

CREATE TABLE test.products (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE test.reviews (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_ref INT REFERENCES test.users(id), -- nullable for possible anonymity
    product_ref INT REFERENCES test.products(id) ON DELETE CASCADE NOT NULL,
    vendor_ref INT REFERENCES test.vendors(id) ON DELETE CASCADE NOT NULL,
    rating INT NOT NULL,
    units_purchased INT NOT NULL,
    unit TEXT NOT NULL,
    price_per_unit DECIMAL(19, 4) NOT NULL,
    comments TEXT
);

CREATE TABLE test.images (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    data TEXT NOT NULL,
    review_ref INT REFERENCES test.reviews(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE test.tags (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE test.tags_to_reviews (
    tag_ref INT REFERENCES test.tags(id) ON DELETE CASCADE NOT NULL,
    review_ref INT REFERENCES test.reviews(id) ON DELETE CASCADE NOT NULL,

    PRIMARY KEY(review_ref, tag_ref)
);


INSERT INTO test.users(name, password_hash) VALUES('mike', 'pass1');
INSERT INTO test.users(name, password_hash) VALUES('shaun', 'pass2');
INSERT INTO test.users(name, password_hash) VALUES('pedro', 'pass3');

INSERT INTO test.products(name) VALUES('grass');
INSERT INTO test.products(name) VALUES('cup');
INSERT INTO test.products(name) VALUES('laptop');

INSERT INTO test.vendors(name, location, phone_no) VALUES('amazon', 'www.amazon.com', 6912345678);
INSERT INTO test.vendors(name, location, phone_no) VALUES('newegg', 'www.newegg.com', 9876);
INSERT INTO test.vendors(name, location, phone_no) VALUES('google play store', 'www.googleplaystore.com', 12629);

INSERT INTO test.tags(name) VALUES('fast delivery');
INSERT INTO test.tags(name) VALUES('slow delivery');
INSERT INTO test.tags(name) VALUES('cheap');
INSERT INTO test.tags(name) VALUES('awful quality');

INSERT INTO test.reviews(
    user_ref,
    product_ref,
    vendor_ref,
    rating,
    units_purchased,
    unit,
    price_per_unit,
    comments
) VALUES(
    (
        SELECT id FROM test.users
        WHERE name = 'mike'
    ),
    (
        SELECT id FROM test.products
        WHERE name = 'grass'
    ),
    (
        SELECT id FROM test.vendors
        WHERE name = 'amazon'
    ),
    0,
    1,
    2,
    3.14,
    'great stuff'
);
INSERT INTO test.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM test.tags
        WHERE name = 'fast delivery'
    ),
    (
        SELECT id FROM test.reviews
        WHERE id = 1
    )
);

INSERT INTO test.reviews(
    user_ref,
    product_ref,
    vendor_ref,
    rating,
    units_purchased,
    unit,
    price_per_unit,
    comments
) VALUES(
    (
        SELECT id FROM test.users
        WHERE name = 'shaun'
    ),
    (
        SELECT id FROM test.products
        WHERE name = 'grass'
    ),
    (
        SELECT id FROM test.vendors
        WHERE name = 'amazon'
    ),
    0,
    1,
    2,
    3.14,
    'grass was rotten'
);
INSERT INTO test.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM test.tags
        WHERE name = 'fast delivery'
    ),
    (
        SELECT id FROM test.reviews
        WHERE id = 2
    )
);
INSERT INTO test.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM test.tags
        WHERE name = 'awful quality'
    ),
    (
        SELECT id FROM test.reviews
        WHERE id = 2
    )
);

INSERT INTO test.reviews(
    user_ref,
    product_ref,
    vendor_ref,
    rating,
    units_purchased,
    unit,
    price_per_unit,
    comments
) VALUES(
    (
        SELECT id FROM test.users
        WHERE name = 'shaun'
    ),
    (
        SELECT id FROM test.products
        WHERE name = 'cup'
    ),
    (
        SELECT id FROM test.vendors
        WHERE name = 'newegg'
    ),
    0,
    1,
    2,
    3.14,
    'broken on arrival'
);
INSERT INTO test.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM test.tags
        WHERE name = 'awful quality'
    ),
    (
        SELECT id FROM test.reviews
        WHERE id = 3
    )
);

INSERT INTO test.reviews(
    user_ref,
    product_ref,
    vendor_ref,
    rating,
    units_purchased,
    unit,
    price_per_unit,
    comments
) VALUES(
    (
        SELECT id FROM test.users
        WHERE name = 'pedro'
    ),
    (
        SELECT id FROM test.products
        WHERE name = 'laptop'
    ),
    (
        SELECT id FROM test.vendors
        WHERE name = 'google play store'
    ),
    0,
    1,
    2,
    3.14,
    'horrible laptop'
);
INSERT INTO test.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM test.tags
        WHERE name = 'awful quality'
    ),
    (
        SELECT id FROM test.reviews
        WHERE id = 4
    )
);

UPDATE test.reviews
SET rating = rating + 1
WHERE id = 1;

UPDATE test.reviews
SET rating = rating + 1
WHERE id = 1;

SELECT r.*
FROM test.reviews r
JOIN test.tags_to_reviews t_r ON r.id = t_r.review_ref
JOIN test.tags t ON t.id = t_r.tag_ref
WHERE t.name = 'awful quality'
;

SELECT
    r.*,
    v.*
FROM
    test.reviews r
        JOIN test.tags_to_reviews t_r ON r.id = t_r.review_ref
        JOIN test.tags t ON t.id = t_r.tag_ref,
    test.vendors v
WHERE
    t.name = 'awful quality'
    AND v.id = r.vendor_ref
;

SELECT
    r.user_ref AS userid,
    r.rating,
    r.units_purchased,
    r.unit,
    r.price_per_unit,
    r.comments,
    v.name,
    v.location,
    v.phone_no,
    p.name AS product_name
FROM
    test.reviews r
        JOIN test.tags_to_reviews t_r ON r.id = t_r.review_ref
        JOIN test.tags t ON t.id = t_r.tag_ref,
    test.vendors v,
    test.products p
WHERE
    t.name = 'awful quality'
    AND v.id = r.vendor_ref
    AND p.id = r.product_ref
;

SELECT
    t.name
FROM
    test.reviews r
        JOIN test.tags_to_reviews tr ON r.id = tr.review_ref,
    test.tags t
WHERE
    t.id = tr.tag_ref
    AND r.id = 1
;

SELECT
    r.*,
    u.*
FROM
    test.reviews r,
    test.users u
WHERE
    r.user_ref = u.id
    AND u.name = 'shaun'
;

SELECT
    r.id AS reviewid,
    r.user_ref AS userid,
    r.rating,
    r.units_purchased,
    r.unit,
    r.price_per_unit,
    r.comments,
    v.name,
    v.location,
    v.phone_no,
    p.name AS product_name
FROM
    test.reviews r,
    test.vendors v,
    test.products p,
    test.users u
WHERE
    v.id = r.vendor_ref
    AND p.id = r.product_ref
    AND r.user_ref = u.id
    AND u.name = ?

