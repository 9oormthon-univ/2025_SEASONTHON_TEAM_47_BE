-- H2: MERGE ... KEY(id) 는 있으면 UPDATE, 없으면 INSERT
MERGE INTO member (id, email, name, created_at, updated_at)
    KEY(id)
    VALUES (1, 'local@test.example', 'Local Test User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
