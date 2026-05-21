package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.controller.shopping.cart.CartController;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return null;
    }

    @PutMapping
    public ShoppingCartDto putProductInCart(@RequestParam String username,
                                     @RequestBody Map<UUID, Integer> productCart) {
        return null;
    }

    @DeleteMapping
    public void deleteShoppingCart(@RequestParam String username) {

    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username,
                                          @RequestBody List<UUID> productId) {
        return null;
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInCart(@RequestParam String username,
                                                @RequestBody ChangeProductQuantityRequest request) {
        return null;
    }

}
