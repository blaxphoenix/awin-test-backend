CREATE TABLE todos
(
    id          BIGSERIAL PRIMARY KEY,
    userid      BIGINT  NOT NULL,
    description TEXT    NOT NULL,
    icon        VARCHAR(255),
    state       BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user FOREIGN KEY (userid) REFERENCES users (userid)
);
