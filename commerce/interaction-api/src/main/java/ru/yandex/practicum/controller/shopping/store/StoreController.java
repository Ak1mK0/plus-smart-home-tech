package ru.yandex.practicum.controller.shopping.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductCategoryDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.QuantityStateDto;

import java.util.List;
import java.util.UUID;

public interface StoreController {

    @GetMapping
    Page<ProductDto> getListOfProducts(@RequestParam ProductCategoryDto category,
                                       @PageableDefault(size = 20, sort = "productName",
                                               direction = Sort.Direction.ASC) Pageable pageable);

    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto product);

    @PostMapping
    ProductDto updateProductInfo(@RequestBody ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    ProductDto changeQuantityState(@RequestParam UUID productId,
                                   @RequestParam QuantityStateDto quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable UUID id);
}
