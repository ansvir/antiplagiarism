DROP TABLE IF EXISTS anti_plagiarism.text_test;
DROP TABLE IF EXISTS anti_plagiarism.user_profile;
DROP TABLE IF EXISTS anti_plagiarism.authorities;
DROP TABLE IF EXISTS anti_plagiarism.group_authorities;
DROP TABLE IF EXISTS anti_plagiarism.group_members;
DROP TABLE IF EXISTS anti_plagiarism.groups;
DROP TABLE IF EXISTS anti_plagiarism.app_user;

CREATE TABLE IF NOT EXISTS anti_plagiarism.app_user
(
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS anti_plagiarism.authorities
(
    username  VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES anti_plagiarism.app_user (username)
);
CREATE UNIQUE INDEX ix_auth_username ON anti_plagiarism.authorities (username, authority);

CREATE TABLE IF NOT EXISTS anti_plagiarism.groups
(
    id         BIGINT      PRIMARY KEY AUTO_INCREMENT,
    group_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS anti_plagiarism.group_authorities
(
    group_id  BIGINT      NOT NULL AUTO_INCREMENT,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id) REFERENCES anti_plagiarism.groups (id)
);

CREATE TABLE IF NOT EXISTS anti_plagiarism.group_members
(
    id       BIGINT      PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    group_id BIGINT      NOT NULL,
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES anti_plagiarism.groups (id)
);

CREATE TABLE IF NOT EXISTS anti_plagiarism.persistent_logins
(
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) PRIMARY KEY,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL
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