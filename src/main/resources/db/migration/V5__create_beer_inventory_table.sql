CREATE TABLE beer_inventory (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    version INTEGER,
    quantity_on_hand INTEGER,
    beer_id INTEGER,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    CONSTRAINT fk_beer_inventory_beer FOREIGN KEY (beer_id) REFERENCES beer(id)
);
