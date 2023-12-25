-- Insert roles if they don't exist
INSERT INTO ec_role (name, created_at, updated_at)
SELECT 'SUPER_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM ec_role WHERE name = 'SUPER_ADMIN');

INSERT INTO ec_role (name, created_at, updated_at)
SELECT 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM ec_role WHERE name = 'ADMIN');

INSERT INTO ec_role (name, created_at, updated_at)
SELECT 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM ec_role WHERE name = 'USER');

-- Insert authorities if they don't exist
INSERT INTO ec_authority (name)
SELECT 'USER_READ'
    WHERE NOT EXISTS (SELECT 1 FROM ec_authority WHERE name = 'USER_READ');

INSERT INTO ec_authority (name)
SELECT 'USER_WRITE'
    WHERE NOT EXISTS (SELECT 1 FROM ec_authority WHERE name = 'USER_WRITE');

INSERT INTO ec_authority (name)
SELECT 'USER_UPDATE'
    WHERE NOT EXISTS (SELECT 1 FROM ec_authority WHERE name = 'USER_UPDATE');

INSERT INTO ec_authority (name)
SELECT 'USER_DELETE'
    WHERE NOT EXISTS (SELECT 1 FROM ec_authority WHERE name = 'USER_DELETE');
