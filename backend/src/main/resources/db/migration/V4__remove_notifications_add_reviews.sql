DROP TABLE IF EXISTS notification;

CREATE TABLE IF NOT EXISTS freelancer_review (
    freelancer_id BIGINT REFERENCES freelancer_profile(id),
    review_text VARCHAR(1000)
);
