package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.BookedProduct;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.UUID;

public interface WarehouseService {

    Product saveProductInWarehouse(Product product);

    BookedProduct checkShoppingCart(ShoppingCart cart);

    void addQuantityInProduct(UUID id, int quantity);

    Address getWarehouseAddress();
}
