ALTER TABLE user_account ADD COLUMN email VARCHAR(255);
ALTER TABLE user_account ADD COLUMN password VARCHAR(255);

UPDATE user_account SET email = 'client@tasklink.dev', password = 'client123' WHERE id = 1;
UPDATE user_account SET email = 'freelancer@tasklink.dev', password = 'freelancer123' WHERE id = 2;

ALTER TABLE user_account ALTER COLUMN email SET NOT NULL;
ALTER TABLE user_account ALTER COLUMN password SET NOT NULL;
ALTER TABLE user_account ADD CONSTRAINT uk_user_account_email UNIQUE (email);
