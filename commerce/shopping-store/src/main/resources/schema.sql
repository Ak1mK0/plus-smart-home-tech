CREATE TYPE quantity_state_enum AS ENUM ('ENDED', 'FEW', 'ENOUGH', 'MANY');
CREATE TYPE product_state_enum AS ENUM ('ACTIVE', 'DEACTIVATE');
CREATE TYPE product_category_enum AS ENUM ('LIGHTING', 'CONTROL', 'SENSORS');

CREATE TABLE IF NOT EXISTS shopping_store (
    id UUID PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_src VARCHAR(255),
    quantity_state quantity_state_enum NOT NULL,
    product_state product_state_enum NOT NULL,
    product_category product_category_enum NOT NULL,
    price DOUBLE PRECISION NOT NULL CHECK (price >= 1.0)
);