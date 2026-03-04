CREATE TABLE IF NOT EXISTS client_profile (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE REFERENCES user_account(id),
    company_info VARCHAR(500)
);
