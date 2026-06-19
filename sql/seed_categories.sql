-- Fashion Store - Category seed data
USE fashionstore;

INSERT INTO categories (category_id, category_name, description) VALUES
(1, 'Women', 'Women Fashion Collection'),
(2, 'Men', 'Men Fashion Collection'),
(3, 'Accessories', 'Fashion Accessories'),
(4, 'Footwear', 'Shoes and Footwear')
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    description = VALUES(description);
