package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface StoreService {

    Product createNewProduct(Product product);

    Product updateProduct(Product product);

    boolean deleteProduct(UUID productId);

    Product getProductInfo(UUID id);
}
