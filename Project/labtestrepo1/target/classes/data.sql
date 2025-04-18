-- Insert test parent user
INSERT INTO users (email, password) VALUES
('parent@test.com', '$2a$10$rDm0mT3D8KGcwgwqdG3Stu9M7e3NB5.DiQI.ER0ZRBC5n2ayWENi.'); -- password: test123

-- Insert test child user
INSERT INTO users (email, password) VALUES
('child@test.com', '$2a$10$rDm0mT3D8KGcwgwqdG3Stu9M7e3NB5.DiQI.ER0ZRBC5n2ayWENi.'); -- password: test123

-- Create parent account
INSERT INTO accounts (user_id, account_type, balance)
SELECT id, 'PARENT', 1000.00
FROM users
WHERE email = 'parent@test.com';

-- Create child account
INSERT INTO accounts (user_id, account_type, balance)
SELECT id, 'CHILD', 100.00
FROM users
WHERE email = 'child@test.com';

-- Create parent-child relationship
INSERT INTO parent_child_relationships (parent_account_id, child_account_id)
SELECT p.id, c.id
FROM accounts p
JOIN accounts c
WHERE p.account_type = 'PARENT'
AND c.account_type = 'CHILD';

-- Insert some sample transactions
INSERT INTO transactions (from_account_id, to_account_id, amount, transaction_type)
SELECT p.id, c.id, 50.00, 'TRANSFER'
FROM accounts p
JOIN accounts c
WHERE p.account_type = 'PARENT'
AND c.account_type = 'CHILD';