package ru.yandex.practicum.controller.shopping.store;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductCategoryDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

public interface StoreController {

    @GetMapping
    Page<ProductDto> getListOfProducts(@RequestParam ProductCategoryDto category,
                                       @RequestParam int page,
                                       @RequestParam int size,
                                       @RequestParam List<String> sort);

    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto product);

    @PostMapping
    ProductDto updateProductInfo(@RequestBody ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean changeQuantityState(@RequestBody SetProductQuantityStateRequest quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable UUID id);
}
