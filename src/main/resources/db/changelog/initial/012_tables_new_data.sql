INSERT INTO users (phone_number, username, password, enabled, role_id)
VALUES ('996 (700) 00-00-03', 'user1', '$2y$10$fp.GlNCVhkvtMD.1WBdAKuRAoxwoA9pFLbF4166lc1ZkETCptKatq', true, (SELECT id FROM roles WHERE role = 'APPLICANT'));

INSERT INTO users (phone_number, username, password, enabled, role_id)
VALUES ('996 (700) 00-00-04', 'user2', '$2y$10$fp.GlNCVhkvtMD.1WBdAKuRAoxwoA9pFLbF4166lc1ZkETCptKatq', true, (SELECT id FROM roles WHERE role = 'APPLICANT'));

INSERT INTO accounts (user_id, currency_id, balance, account_number)
VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM currencies WHERE currency_name = 'USD'), 100.00, '12345678912345678914');

INSERT INTO accounts (user_id, currency_id, balance, account_number)
VALUES ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM currencies WHERE currency_name = 'USD'), 150.00, '12345678912345678915');
