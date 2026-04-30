package ru.yandex.practicum.telemetry.analyzer.producer;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import telemetry.service.event.DeviceActionRequestProto;

@Slf4j
@Component
public class HubRouterProducer {

    @GrpcClient("client")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub controller;

    private void send(DeviceActionRequestProto request) {
        controller.handleDeviceAction(request);
    }
}
