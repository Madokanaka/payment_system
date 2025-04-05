INSERT INTO users (phone_number, username, password, is_enabled, role_id)
VALUES
    ('996 (700) 00-00-01', 'admin', '$2y$10$fp.GlNCVhkvtMD.1WBdAKuRAoxwoA9pFLbF4166lc1ZkETCptKatq', true, (SELECT id FROM roles WHERE role = 'ADMIN')),
    ('996 (700) 00-00-02', 'applicant', '$2y$10$fp.GlNCVhkvtMD.1WBdAKuRAoxwoA9pFLbF4166lc1ZkETCptKatq', true, (SELECT id FROM roles WHERE role = 'APPLICANT'));

INSERT INTO currencies (currency_name)
VALUES
    ('USD'),
    ('EUR'),
    ('KGS');

INSERT INTO accounts (user_id, currency_id, balance, account_number)
VALUES
    ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM currencies WHERE currency_name = 'USD'), 100.00, '12345678912345678912'),
    ((SELECT id FROM users WHERE username = 'applicant'), (SELECT id FROM currencies WHERE currency_name = 'USD'), 50.00, '12345678912345678913');

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, status, transaction_type, created_at)
VALUES
    ((SELECT id FROM accounts WHERE account_number = '12345678912345678912'), (SELECT id FROM accounts WHERE account_number = '12345678912345678913'), 20.00, 'PENDING', 'TRANSFER', NOW());
