-- Insert default role
INSERT INTO ec_role (name, created_at, updated_at) VALUES ('SUPER_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO ec_role (name, created_at, updated_at) VALUES ('ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO ec_role (name, created_at, updated_at) VALUES ('USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert authorities
INSERT INTO ec_authority (name) VALUES ('USER_READ');
INSERT INTO ec_authority (name) VALUES ('USER_WRITE');
INSERT INTO ec_authority (name) VALUES ('USER_UPDATE');
INSERT INTO ec_authority (name) VALUES ('USER_DELETE');
