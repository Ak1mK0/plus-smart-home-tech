package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.ProductState;
import ru.yandex.practicum.repository.StoreRepository;
import ru.yandex.practicum.service.StoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Loggable
    @Transactional
    public Product createNewProduct(Product product) {
        return storeRepository.save(product);
    }

    @Loggable
    @Transactional
    public Product updateProduct(Product product) {
        if (storeRepository.existsById(product.getProductId())) {
            return storeRepository.save(product);
        } else {
            throw new ProductNotFoundException("Product with ID: " + product.getProductId() + "don't present");
        }
    }

    @Loggable
    @Transactional
    public boolean deleteProduct(UUID productId) {
        Optional<Product> productOptional = storeRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductState(ProductState.DEACTIVATE);
            return true;
        } else {
            throw new ProductNotFoundException("Product with ID: " + productId + "don't present");
        }
    }

    @Loggable
    public Product getProductInfo(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Not found product with ID: " + id));
    }

    @Loggable
    public Page<Product> getListOfProducts(ProductCategory category,
                                           int page,
                                           int size,
                                           List<String> sorts) {
        List<Sort.Order> order = new ArrayList<>();
        for (String sort : sorts) {
            String[] sorting = sort.split(",");
            Sort.Direction direction = Sort.Direction.ASC;
            if (sorting.length > 1) {
                if ("desc".equalsIgnoreCase(sorting[1].trim())) {
                    direction = Sort.Direction.DESC;
                }
            }
            order.add(new Sort.Order(direction, sorting[0]));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return storeRepository.findByProductCategory(category, pageable);
    }
}
