DROP SCHEMA test CASCADE;

CREATE SCHEMA test;

CREATE TABLE test.users (
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE test.vendors (
    vendor_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vendor_name TEXT NOT NULL,
    vendor_address TEXT NOT NULL
);

CREATE TABLE test.reviews (
    review_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    body TEXT NOT NULL,
    user_ref INT REFERENCES test.users(user_id),
    vendor_ref INT REFERENCES test.vendors(vendor_id),
    like_count INT NOT NULL
);

CREATE TABLE test.goods (
    good_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    good_name TEXT NOT NULL
);

CREATE TABLE test.goods_to_vendors (
    good_ref INT REFERENCES test.goods(good_id),
    vendor_ref INT REFERENCES test.vendors(vendor_id),

    PRIMARY KEY(good_ref, vendor_ref)
);

CREATE TABLE test.goods_to_reviews (
    good_ref INT REFERENCES test.goods(good_id),
    review_ref INT REFERENCES test.reviews(review_id),

    PRIMARY KEY(good_ref, review_ref)
);


INSERT INTO test.users(user_name, password) VALUES('mike', 'pass1');

INSERT INTO test.vendors(vendor_name, vendor_address) VALUES('amazon', 'www.amazon.com');

INSERT INTO test.reviews(user_ref, vendor_ref, body, like_count) VALUES(
    (
        SELECT user_id FROM test.users
        WHERE user_name = 'mike'
    ),
    (
        SELECT vendor_id FROM test.vendors
        WHERE vendor_name = 'amazon'
    ),
    'great stuff',
    0
);

UPDATE test.reviews
SET like_count = like_count + 1
WHERE review_id = 1;

UPDATE test.reviews
SET like_count = like_count + 1
WHERE review_id = 1;