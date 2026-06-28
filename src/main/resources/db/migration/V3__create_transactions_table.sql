CREATE TABLE transactions
(
    id      BIGSERIAL PRIMARY KEY,
    userid  BIGINT NOT NULL,
    "value"   DOUBLE PRECISION NOT NULL,
    details VARCHAR(255) NOT NULL,
    date    DATE NOT NULL,
    CONSTRAINT fk_user_transactions FOREIGN KEY (userid) REFERENCES users (userid)
);
