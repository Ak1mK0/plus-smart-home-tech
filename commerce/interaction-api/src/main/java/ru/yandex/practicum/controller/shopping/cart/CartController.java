package ru.yandex.practicum.controller.shopping.cart;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface CartController {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PutMapping
    ShoppingCartDto putProductInCart(@RequestParam String username,
                                     @RequestBody Map<UUID, Integer> productCart);

    @DeleteMapping
    void deleteShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductFromCart(@RequestParam String username,
                                          @RequestBody List<UUID> productId);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantityInCart(@RequestParam String username,
                                                @RequestBody ChangeProductQuantityRequest request);

}
