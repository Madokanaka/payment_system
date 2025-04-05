INSERT INTO transactions (sender_account_id, receiver_account_id, amount, status, transaction_type, created_at, approved_at)
VALUES (
           (SELECT id FROM accounts WHERE account_number = '12345678912345678914'),
           (SELECT id FROM accounts WHERE account_number = '12345678912345678915'),
           20.00,
           'ROLLED_BACK',
           'TRANSFER',
           '2025-04-05 10:00:00',
           NULL
       ),
       (
           (SELECT id FROM accounts WHERE account_number = '12345678912345678914'),
           (SELECT id FROM accounts WHERE account_number = '12345678912345678915'),
           30.00,
           'PENDING',
           'TRANSFER',
           '2025-04-05 12:00:00',
           NULL
       );

INSERT INTO transactions (sender_account_id, receiver_account_id, amount, status, transaction_type, created_at, approved_at)
VALUES (
           (SELECT id FROM accounts WHERE account_number = '12345678912345678915'),
           (SELECT id FROM accounts WHERE account_number = '12345678912345678914'),
           50.00,
           'APPROVED',
           'TRANSFER',
           '2025-04-05 11:00:00',
           '2025-04-05 11:30:00'
       );

INSERT INTO transaction_rollbacks (transaction_id, rollbacked_at, amount)
VALUES (
           (SELECT id FROM transactions
            WHERE sender_account_id = (SELECT id FROM accounts WHERE account_number = '12345678912345678914')
              AND receiver_account_id = (SELECT id FROM accounts WHERE account_number = '12345678912345678915')
              AND amount = 20.00),
           '2025-04-05 12:00:00',
           20.00
       );