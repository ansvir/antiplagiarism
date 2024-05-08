DELETE FROM anti_plagiarism.text_test;
DELETE FROM anti_plagiarism.user_profile;
DELETE FROM anti_plagiarism.authorities;
DELETE FROM anti_plagiarism.group_authorities;
DELETE FROM anti_plagiarism.group_members;
DELETE FROM anti_plagiarism.groups;
DELETE FROM anti_plagiarism.app_user;

INSERT INTO anti_plagiarism.app_user (username, password, enabled)
VALUES ('admin', '$2a$12$jruDXcLXBjFnZ8bi5ZOZhuskgv/MCpwUkHNXwfLsYAGr.MXbz1s1m', true);

INSERT INTO anti_plagiarism.groups VALUES (1, 'USER');

INSERT INTO anti_plagiarism.authorities VALUES ('admin', 'USER');

INSERT INTO anti_plagiarism.group_authorities VALUES (1, 'USER');

INSERT INTO anti_plagiarism.user_profile
VALUES (1, 'admin');