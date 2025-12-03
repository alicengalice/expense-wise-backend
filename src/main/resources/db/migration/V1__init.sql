-- =============================
-- USERS TABLE
-- =============================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);

-- =============================
-- CATEGORIES TABLE
-- =============================
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- =============================
-- EXPENSES TABLE
-- =============================
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    date DATE,
    category_id BIGINT,
    user_id BIGINT,

    CONSTRAINT fk_expenses_category
        FOREIGN KEY (category_id) REFERENCES categories(id),

    CONSTRAINT fk_expenses_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);