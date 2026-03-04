CREATE TABLE user_account (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(40) NOT NULL
);

CREATE TABLE skill (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    category VARCHAR(255) NOT NULL
);

CREATE TABLE freelancer_profile (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE REFERENCES user_account(id),
    about VARCHAR(1000),
    rating DOUBLE PRECISION
);

CREATE TABLE freelancer_skill (
    freelancer_id BIGINT REFERENCES freelancer_profile(id),
    skill_id BIGINT REFERENCES skill(id)
);

CREATE TABLE freelancer_portfolio (
    freelancer_id BIGINT REFERENCES freelancer_profile(id),
    portfolio_link VARCHAR(1000)
);

CREATE TABLE task_order (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    category VARCHAR(60) NOT NULL,
    budget NUMERIC(19,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    deadline DATE NOT NULL,
    min_rating DOUBLE PRECISION,
    status VARCHAR(40) NOT NULL,
    urgent BOOLEAN NOT NULL,
    featured BOOLEAN NOT NULL,
    pricing_mode VARCHAR(40) NOT NULL,
    estimated_hours INTEGER,
    client_id BIGINT REFERENCES user_account(id),
    priority_score INTEGER
);

CREATE TABLE proposal (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES task_order(id),
    freelancer_id BIGINT REFERENCES user_account(id),
    price NUMERIC(19,2) NOT NULL,
    message VARCHAR(2000) NOT NULL
);

CREATE TABLE proposal_attachment (
    proposal_id BIGINT REFERENCES proposal(id),
    attachment_link VARCHAR(1000)
);

CREATE TABLE proposal_ability (
    proposal_id BIGINT REFERENCES proposal(id),
    ability VARCHAR(255)
);

INSERT INTO user_account(name, role) VALUES ('Demo Client', 'CLIENT'), ('Demo Freelancer', 'FREELANCER');
INSERT INTO freelancer_profile(user_id, about, rating) VALUES (2, 'I build digital products.', 4.6);

INSERT INTO skill(name, category) VALUES
('Java', 'Development'), ('Spring', 'Development'), ('React', 'Development'), ('Node.js', 'Development'), ('TypeScript', 'Development'),
('SQL', 'Development'), ('PostgreSQL', 'Development'), ('Docker', 'DevOps'), ('Kubernetes', 'DevOps'), ('AWS', 'DevOps'),
('UI/UX', 'Design'), ('Figma', 'Design'), ('Branding', 'Design'), ('Illustration', 'Design'), ('Motion Design', 'Design'),
('SEO', 'Marketing'), ('SMM', 'Marketing'), ('Email Marketing', 'Marketing'), ('PPC', 'Marketing'), ('Analytics', 'Marketing'),
('Copywriting', 'Writing'), ('Technical Writing', 'Writing'), ('Editing', 'Writing'), ('Content Strategy', 'Writing'), ('Storytelling', 'Writing'),
('Video Editing', 'Video'), ('Color Grading', 'Video'), ('Sound Design', 'Video'), ('Animation', 'Video'), ('Script Writing', 'Video'),
('QA Testing', 'Development'), ('Product Management', 'Management'), ('Sales Funnel', 'Marketing'), ('WordPress', 'Development'), ('Python', 'Development'),
('Graphic Design', 'Design'), ('Canva', 'Design'), ('Research', 'Writing'), ('Cinematography', 'Video'), ('Prompt Engineering', 'Development');

INSERT INTO freelancer_skill(freelancer_id, skill_id)
SELECT 1, id FROM skill WHERE name IN ('Java', 'Spring', 'React', 'SQL');

INSERT INTO freelancer_portfolio(freelancer_id, portfolio_link) VALUES
(1, 'https://portfolio.example.com/project-1'),
(1, 'https://portfolio.example.com/project-2');

insert into user_account (name, role) values ('Test Client', 'CLIENT');