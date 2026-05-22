package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.BookedProduct;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.service.WarehouseService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Loggable
    @Transactional
    public Product saveProductInWarehouse(Product product) {
        UUID id = product.getProductId();
        if (warehouseRepository.existsById(id)) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product with ID: " + id + " already exists!");
        }
        return warehouseRepository.save(product);
    }

    @Loggable
    public BookedProduct checkShoppingCart(ShoppingCart cart) {
        return null;
    }

    @Loggable
    @Transactional
    public void addQuantityInProduct(UUID id, int quantity) {
        Product product = warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Not found product with ID: " + id));
            product.setQuantity(quantity);
            warehouseRepository.save(product);
    }

    @Loggable
    public Address getWarehouseAddress() {
        return Address.builder()
                .country("Country")
                .city("City")
                .street("Street")
                .house("House")
                .flat("Flat")
                .build();
    }
}
