-- Fashion Store - Full database setup
-- Run: mysql -u root -p < sql/setup.sql

CREATE DATABASE IF NOT EXISTS fashionstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fashionstore;

SOURCE schema.sql;
SOURCE seed_categories.sql;
SOURCE seed_admin.sql;
SOURCE seed_products.sql;
