INSERT INTO anti_plagiarism.app_user (username, password, role, enabled)
VALUES ('admin', 'admin', 'USER', true);

INSERT INTO anti_plagiarism.profile (user_id)
VALUES ('admin')