CREATE TABLE beer (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    version INTEGER,
    beer_name VARCHAR(255),
    beer_style VARCHAR(255),
    upc VARCHAR(255),
    quantity_on_hand INTEGER,
    price DECIMAL(19, 2),
    created_date TIMESTAMP,
    update_date TIMESTAMP
);