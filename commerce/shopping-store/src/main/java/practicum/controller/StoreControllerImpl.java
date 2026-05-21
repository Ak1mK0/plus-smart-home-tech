package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.controller.shopping.store.StoreController;
import ru.yandex.practicum.dto.PageProductDto;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.logging.Loggable;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreControllerImpl implements StoreController {

    @Loggable
    @GetMapping
    public PageProductDto getListOfProducts(@RequestParam ProductCategory category,
                                     @RequestParam int page,
                                     @RequestParam int size,
                                     @RequestParam ArrayList<String> sort) {
        return null;
    }

    @Loggable
    @PutMapping
    public ProductDto createNewProduct(@RequestBody ProductDto product) {
        return null;
    }

    @Loggable
    @PostMapping
    public ProductDto updateProductInfo(@RequestBody ProductDto product) {
        return null;
    }

    @Loggable
    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        return false;
    }

    @Loggable
    @PostMapping("/quantityState")
    public boolean changeQuantityState(@RequestBody SetProductQuantityStateRequest quantityState) {
        return false;
    }

    @Loggable
    @GetMapping("/{productId}")
    public ProductDto getProductInfo(@PathVariable UUID id,
                              @RequestBody UUID productId) {
        return null;
    }
}
