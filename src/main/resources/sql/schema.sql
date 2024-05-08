DROP TABLE IF EXISTS anti_plagiarism.text_test;
DROP TABLE IF EXISTS anti_plagiarism.user_profile;
DROP TABLE IF EXISTS anti_plagiarism.app_user;

CREATE TABLE IF NOT EXISTS anti_plagiarism.app_user
(
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS anti_plagiarism.user_profile
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_profile_app_user FOREIGN KEY (user_id) REFERENCES anti_plagiarism.app_user (username)
);

CREATE TABLE IF NOT EXISTS anti_plagiarism.text_test
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    text_one        TEXT NOT NULL,
    text_two        TEXT NOT NULL,
    plagiat_result  DECIMAL(2, 2) NOT NULL,
    user_profile_id BIGINT NOT NULL,
    CONSTRAINT fk_text_test_user_profile FOREIGN KEY (user_profile_id) REFERENCES anti_plagiarism.user_profile (id)
);