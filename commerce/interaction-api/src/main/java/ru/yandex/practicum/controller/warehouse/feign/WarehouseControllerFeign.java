package ru.yandex.practicum.controller.warehouse.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.controller.warehouse.WarehouseController;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseControllerFeign extends WarehouseController {
}
