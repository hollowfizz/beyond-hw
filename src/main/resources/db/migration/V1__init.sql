CREATE TABLE IF NOT EXISTS users
(
    id          INT          NOT NULL AUTO_INCREMENT UNIQUE,
    username    VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    create_date DATE,
    update_date DATE
);

CREATE TABLE IF NOT EXISTS tasks
(
    id          INT NOT NULL AUTO_INCREMENT UNIQUE,
    name        VARCHAR(100),
    description VARCHAR(100),
    date_time   DATE,
    status      VARCHAR(100),
    user_id     INT,
    create_date DATE,
    update_date DATE,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS logs
(
    id        INT NOT NULL AUTO_INCREMENT UNIQUE,
    message   VARCHAR(255),
    log_level VARCHAR(100),
    exception CLOB,
    log_time  TIMESTAMP
);