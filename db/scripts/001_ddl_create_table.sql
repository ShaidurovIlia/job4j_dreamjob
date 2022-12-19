CREATE TABLE IF NOT EXISTS post
(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TEXT,
    visible     bool,
    city_id     INT
);

CREATE TABLE IF NOT EXISTS candidate
(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
	created     TEXT,
    visible     bool,
    city_id     INT,
    photo       BYTEA
);

CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY,
    name         TEXT,
    email        VARCHAR UNIQUE,
    password     TEXT
);

