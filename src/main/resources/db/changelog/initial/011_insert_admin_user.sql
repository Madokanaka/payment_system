INSERT INTO users (phone_number, username, password, enabled, role_id)
VALUES ('admin', 'admin', '$2y$10$fp.GlNCVhkvtMD.1WBdAKuRAoxwoA9pFLbF4166lc1ZkETCptKatq', true, (SELECT id FROM roles WHERE role = 'ADMIN'));
