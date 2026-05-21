package ru.yandex.practicum.controller.shopping.store;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PageProductDto;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;

import java.util.ArrayList;
import java.util.UUID;

public interface StoreController {

    @GetMapping
    PageProductDto getListOfProducts(@RequestParam ProductCategory category,
                                     @RequestParam int page,
                                     @RequestParam int size,
                                     @RequestParam ArrayList<String> sort);

    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto product);

    @PostMapping
    ProductDto updateProductInfo(@RequestBody ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean changeQuantityState(@RequestBody SetProductQuantityStateRequest quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable UUID id,
                              @RequestBody UUID productId);
}
