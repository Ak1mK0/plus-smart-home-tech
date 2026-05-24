package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.controller.shopping.cart.CartController;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.mapper.ShoppingCartMapper;
import ru.yandex.practicum.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {
    private final CartService cartService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Loggable
    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        ShoppingCart shoppingCart = cartService.getUserProductCart(username);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Loggable
    @PutMapping
    public ShoppingCartDto putProductInCart(@RequestParam String username,
                                            @RequestBody Map<UUID, Integer> productCart) {
        ShoppingCart shoppingCart = cartService.putProductInCart(username, productCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Loggable
    @DeleteMapping
    public void deleteShoppingCart(@RequestParam String username) {
        cartService.deleteShoppingCart(username);
    }

    @Loggable
    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username,
                                                 @RequestBody List<UUID> productId) {
        ShoppingCart shoppingCart = cartService.removeProductFromCart(username, productId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Loggable
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInCart(@RequestParam String username,
                                                       @RequestBody ChangeProductQuantityRequest request) {
        ShoppingCart shoppingCart = cartService.changeQuantityInShoppingCart(username,
                                                                            request.getProductId(),
                                                                            request.getNewQuantity());
        return shoppingCartMapper.toDto(shoppingCart);
    }

}
