CREATE TABLE beer_order (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    version INTEGER,
    order_status VARCHAR(50),
    order_status_callback_url VARCHAR(255),
    customer_id INTEGER,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    CONSTRAINT fk_beer_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);
