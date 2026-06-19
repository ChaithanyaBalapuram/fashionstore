-- Fashion Store - Default admin account
-- Username: admin  |  Password: admin123
-- Password is auto-hashed to BCrypt on first login if stored as plain text.
USE fashionstore;

INSERT INTO admin (admin_id, username, password) VALUES
(1, 'admin', 'admin123')
ON DUPLICATE KEY UPDATE username = VALUES(username);
