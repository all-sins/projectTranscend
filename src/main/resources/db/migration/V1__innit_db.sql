CREATE TABLE IF NOT EXISTS items (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255),
    item_amount INT,
    item_damage INT
);
