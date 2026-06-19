USE fashionstore;

INSERT INTO products (category_id, product_name, brand, description, original_price, discount_price, stock, featured, new_arrival, active)
SELECT 2, 'Classic Cotton T-Shirt', 'MOVA', 'Premium cotton t-shirt for everyday comfort', 1299.00, 999.00, 100, 1, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Classic Cotton T-Shirt');

INSERT INTO products (category_id, product_name, brand, description, original_price, discount_price, stock, featured, new_arrival, active)
SELECT 3, 'Leather Belt', 'MOVA', 'Genuine leather belt with metal buckle', 899.00, 699.00, 50, 0, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Leather Belt');

INSERT INTO products (category_id, product_name, brand, description, original_price, discount_price, stock, featured, new_arrival, active)
SELECT 4, 'Running Sneakers', 'MOVA', 'Lightweight running shoes with cushioned sole', 3999.00, 2999.00, 30, 1, 0, 1
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Running Sneakers');

INSERT INTO products (category_id, product_name, brand, description, original_price, discount_price, stock, featured, new_arrival, active)
SELECT 1, 'Floral Summer Dress', 'MOVA', 'Elegant floral print dress for summer occasions', 2499.00, 1899.00, 40, 1, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name = 'Floral Summer Dress');

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Navy', 'M', 25 FROM products p WHERE p.product_name = 'Classic Cotton T-Shirt'
AND NOT EXISTS (SELECT 1 FROM product_variants v JOIN products p2 ON v.product_id = p2.product_id WHERE p2.product_name = 'Classic Cotton T-Shirt');

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Navy', 'L', 30 FROM products p WHERE p.product_name = 'Classic Cotton T-Shirt'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Classic Cotton T-Shirt') AND color='Navy' AND size='L');

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'White', 'M', 20 FROM products p WHERE p.product_name = 'Classic Cotton T-Shirt'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Classic Cotton T-Shirt') AND color='White' AND size='M');

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Brown', '32', 15 FROM products p WHERE p.product_name = 'Leather Belt'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Leather Belt'));

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Black', '34', 20 FROM products p WHERE p.product_name = 'Leather Belt'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Leather Belt') AND color='Black');

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'White', '8', 10 FROM products p WHERE p.product_name = 'Running Sneakers'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Running Sneakers'));

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Black', '9', 12 FROM products p WHERE p.product_name = 'Running Sneakers'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Running Sneakers') AND color='Black');

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Pink', 'S', 10 FROM products p WHERE p.product_name = 'Floral Summer Dress'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Floral Summer Dress'));

INSERT INTO product_variants (product_id, color, size, stock)
SELECT p.product_id, 'Pink', 'M', 15 FROM products p WHERE p.product_name = 'Floral Summer Dress'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE product_id = (SELECT product_id FROM products WHERE product_name = 'Floral Summer Dress') AND size='M');
