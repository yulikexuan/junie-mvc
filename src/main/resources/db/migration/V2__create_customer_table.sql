CREATE TABLE customer (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    version INTEGER,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_date TIMESTAMP,
    update_date TIMESTAMP
);
