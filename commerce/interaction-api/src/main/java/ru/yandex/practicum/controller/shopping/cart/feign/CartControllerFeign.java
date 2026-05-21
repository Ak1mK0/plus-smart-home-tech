package ru.yandex.practicum.controller.shopping.cart.feign;


import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.controller.shopping.cart.CartController;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartControllerFeign extends CartController {
}
