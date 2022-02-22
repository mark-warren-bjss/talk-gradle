CREATE TABLE greeting_text
(
    id       BIGSERIAL PRIMARY KEY,
    language VARCHAR(20)  NOT NULL,
    content  VARCHAR(200) NOT NULL
);

CREATE UNIQUE INDEX idx_greeting_text__language
    ON greeting_text (language);
