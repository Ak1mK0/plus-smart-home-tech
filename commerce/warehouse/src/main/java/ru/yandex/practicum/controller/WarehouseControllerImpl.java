package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.controller.warehouse.WarehouseController;
import ru.yandex.practicum.dto.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseControllerImpl implements WarehouseController {

    @PutMapping
    public void newProductInWarehouse(@RequestBody NewProductInWarehouseRequest product) {

    }

    @PostMapping("/check")
    public BookedProductsDto checkAvailableAllProductInShoppingCart(@RequestBody ShoppingCartDto shoppingCart) {
        return null;
    }

    @PostMapping("/add")
    public void addQuantityInProduct(@RequestBody AddProductToWarehouseRequest productQuantity) {

    }

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