INSERT INTO users (phone_number, username, password, enabled, role_id)
VALUES ('admin', 'admin', '$2y$10$lPYY3Yt4CIGloq9YXnHNT.1MVzvpYVSm5APq8ZFFhh9ta7BojM7V6', true, (SELECT id FROM roles WHERE role = 'ADMIN'));
