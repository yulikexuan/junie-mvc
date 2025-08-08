CREATE TABLE beer_order_line (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    version INTEGER,
    order_quantity INTEGER,
    quantity_allocated INTEGER,
    beer_order_id INTEGER,
    beer_id INTEGER,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    CONSTRAINT fk_beer_order_line_order FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
    CONSTRAINT fk_beer_order_line_beer FOREIGN KEY (beer_id) REFERENCES beer(id)
);
