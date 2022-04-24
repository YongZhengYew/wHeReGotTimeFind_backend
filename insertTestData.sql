INSERT INTO deployed.users(name, password_hash) VALUES('mike', 'pass1');
INSERT INTO deployed.users(name, password_hash) VALUES('shaun', 'pass2');
INSERT INTO deployed.users(name, password_hash, temp_auth_token) VALUES('pedro', 'pass3', 'tat');

INSERT INTO deployed.products(name) VALUES('grass');
INSERT INTO deployed.products(name) VALUES('cup');
INSERT INTO deployed.products(name) VALUES('laptop');

INSERT INTO deployed.vendors(name, location, phone_no) VALUES('amazon', 'www.amazon.com', 6912345678);
INSERT INTO deployed.vendors(name, location, phone_no) VALUES('newegg', 'www.newegg.com', 9876);
INSERT INTO deployed.vendors(name, location, phone_no) VALUES('google play store', 'www.googleplaystore.com', 12629);

INSERT INTO deployed.tags(name) VALUES('fast delivery');
INSERT INTO deployed.tags(name) VALUES('slow delivery');
INSERT INTO deployed.tags(name) VALUES('cheap');
INSERT INTO deployed.tags(name) VALUES('awful quality');

INSERT INTO deployed.reviews(
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
        SELECT id FROM deployed.users
        WHERE name = 'mike'
    ),
    (
        SELECT id FROM deployed.products
        WHERE name = 'grass'
    ),
    (
        SELECT id FROM deployed.vendors
        WHERE name = 'amazon'
    ),
    0,
    1,
    2,
    3.14,
    'great stuff'
);
INSERT INTO deployed.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM deployed.tags
        WHERE name = 'fast delivery'
    ),
    (
        SELECT id FROM deployed.reviews
        WHERE id = 1
    )
);

INSERT INTO deployed.reviews(
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
        SELECT id FROM deployed.users
        WHERE name = 'shaun'
    ),
    (
        SELECT id FROM deployed.products
        WHERE name = 'grass'
    ),
    (
        SELECT id FROM deployed.vendors
        WHERE name = 'amazon'
    ),
    0,
    1,
    2,
    3.14,
    'grass was rotten'
);
INSERT INTO deployed.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM deployed.tags
        WHERE name = 'fast delivery'
    ),
    (
        SELECT id FROM deployed.reviews
        WHERE id = 2
    )
);
INSERT INTO deployed.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM deployed.tags
        WHERE name = 'awful quality'
    ),
    (
        SELECT id FROM deployed.reviews
        WHERE id = 2
    )
);

INSERT INTO deployed.reviews(
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
        SELECT id FROM deployed.users
        WHERE name = 'shaun'
    ),
    (
        SELECT id FROM deployed.products
        WHERE name = 'cup'
    ),
    (
        SELECT id FROM deployed.vendors
        WHERE name = 'newegg'
    ),
    0,
    1,
    2,
    3.14,
    'broken on arrival'
);
INSERT INTO deployed.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM deployed.tags
        WHERE name = 'awful quality'
    ),
    (
        SELECT id FROM deployed.reviews
        WHERE id = 3
    )
);

INSERT INTO deployed.reviews(
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
        SELECT id FROM deployed.users
        WHERE name = 'pedro'
    ),
    (
        SELECT id FROM deployed.products
        WHERE name = 'laptop'
    ),
    (
        SELECT id FROM deployed.vendors
        WHERE name = 'google play store'
    ),
    0,
    1,
    2,
    3.14,
    'horrible laptop'
);
INSERT INTO deployed.tags_to_reviews(tag_ref, review_ref) VALUES(
    (
        SELECT id FROM deployed.tags
        WHERE name = 'awful quality'
    ),
    (
        SELECT id FROM deployed.reviews
        WHERE id = 4
    )
);

UPDATE deployed.reviews
SET rating = rating + 1
WHERE id = 1;

UPDATE deployed.reviews
SET rating = rating + 1
WHERE id = 1;

SELECT r.*
FROM deployed.reviews r
JOIN deployed.tags_to_reviews t_r ON r.id = t_r.review_ref
JOIN deployed.tags t ON t.id = t_r.tag_ref
WHERE t.name = 'awful quality'
;

SELECT
    r.*,
    v.*
FROM
    deployed.reviews r
        JOIN deployed.tags_to_reviews t_r ON r.id = t_r.review_ref
        JOIN deployed.tags t ON t.id = t_r.tag_ref,
    deployed.vendors v
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
    deployed.reviews r
        JOIN deployed.tags_to_reviews t_r ON r.id = t_r.review_ref
        JOIN deployed.tags t ON t.id = t_r.tag_ref,
    deployed.vendors v,
    deployed.products p
WHERE
    t.name = 'awful quality'
    AND v.id = r.vendor_ref
    AND p.id = r.product_ref
;

SELECT
    t.name
FROM
    deployed.reviews r
        JOIN deployed.tags_to_reviews tr ON r.id = tr.review_ref,
    deployed.tags t
WHERE
    t.id = tr.tag_ref
    AND r.id = 1
;

SELECT
    r.*,
    u.*
FROM
    deployed.reviews r,
    deployed.users u
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
    deployed.reviews r,
    deployed.vendors v,
    deployed.products p,
    deployed.users u
WHERE
    v.id = r.vendor_ref
    AND p.id = r.product_ref
    AND r.user_ref = u.id
    AND u.name = ?
