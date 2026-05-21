package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.controller.warehouse.WarehouseController;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.logging.Loggable;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseControllerImpl implements WarehouseController {

    @Loggable
    @PutMapping
    public void newProductInWarehouse(@RequestBody NewProductInWarehouseRequest product) {

    }

    @Loggable
    @PostMapping("/check")
    public BookedProductsDto checkAvailableAllProductInShoppingCart(@RequestBody ShoppingCartDto shoppingCart) {
        return null;
    }

    @Loggable
    @PostMapping("/add")
    public void addQuantityInProduct(@RequestBody AddProductToWarehouseRequest productQuantity) {

    }

    @Loggable
    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country("Country")
                .sity("Sity")
                .street("Street")
                .house("House")
                .flat("Flat")
                .build();
    }
}