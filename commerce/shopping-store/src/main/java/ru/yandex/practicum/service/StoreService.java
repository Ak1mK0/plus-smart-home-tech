package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    Product createNewProduct(Product product);

    Product updateProduct(Product product);

    boolean deleteProduct(UUID productId);

    Product getProductInfo(UUID id);

    Page<Product> getListOfProducts(ProductCategory category,
                                    int page,
                                    int size,
                                    List<String> sorts);
}
