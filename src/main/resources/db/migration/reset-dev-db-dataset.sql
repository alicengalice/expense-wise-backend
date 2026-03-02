-- Quick Development Database Reset
-- Run this in pgAdmin Query Tool to have fresh data
-- This keeps the schema but clears all data and reloads samples

-- 1. Clear all existing data
TRUNCATE expenses, categories, users RESTART IDENTITY CASCADE;

-- 2. Insert sample users
INSERT INTO users (id, username, email, created_at)
VALUES
  (1, 'alice', 'alice@example.com', NOW()),
  (2, 'bob', 'bob@example.com', NOW()),
  (3, 'charlie', 'charlie@example.com', NOW());

-- 3. Insert sample categories
INSERT INTO categories (id, name, description)
VALUES
  (1, 'Food', 'Meals, snacks, and beverages'),
  (2, 'Groceries', 'Household grocery purchases'),
  (3, 'Transport', 'Opal, Uber'),
  (4, 'Shopping', 'Clothes, accessories, personal items'),
  (5, 'Bills', 'Electricity, water, phone, internet'),
  (6, 'Entertainment', 'Movies, games, fun activities');

-- 4. Insert sample expenses
INSERT INTO expenses (id, description, amount, date, user_id, category_id)
VALUES
-- Alice's expenses
(1, 'Matcha Latte', 6.50, '2025-01-05', 1, 1),
(2, 'Woolworths groceries', 48.20, '2025-01-07', 1, 2),
(3, 'Opal top up', 10.0, '2025-01-08', 1, 3),
(4, 'Kmart skincare haul', 29.99, '2025-01-08', 1, 4),
(5, 'Electricity bill', 120.00, '2025-01-03', 1, 5),

-- Bob's expenses
(6, 'Coffee', 6.50, '2025-01-04', 2, 1),
(7, 'Movie ticket', 18.00, '2025-01-09', 2, 6),
(8, 'Uber ride', 22.70, '2025-01-06', 2, 3),

-- Charlie's expenses
(9, 'Lunch with friends', 15.80, '2025-01-05', 3, 1),
(10, 'Phone bill', 39.99, '2025-01-02', 3, 5),
(11, 'PS5', 79.00, '2025-01-07', 3, 6);

-- 5. Fix sequences to continue from max ID
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));
SELECT setval('expenses_id_seq', (SELECT MAX(id) FROM expenses));
