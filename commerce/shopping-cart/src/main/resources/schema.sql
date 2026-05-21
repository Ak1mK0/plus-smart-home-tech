CREATE TABLE IF NOT EXISTS shopping_cart (
    id UUID,
    product_id UUID,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (id, product_id)
);