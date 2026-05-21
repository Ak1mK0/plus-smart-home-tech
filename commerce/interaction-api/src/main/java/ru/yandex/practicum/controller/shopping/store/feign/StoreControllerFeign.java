package ru.yandex.practicum.controller.shopping.store.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.controller.shopping.store.StoreController;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface StoreControllerFeign extends StoreController {
}
