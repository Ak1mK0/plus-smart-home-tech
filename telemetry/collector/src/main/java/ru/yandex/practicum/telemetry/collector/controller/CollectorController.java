package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import telemetry.service.collector.CollectorControllerGrpc;
import telemetry.service.event.*;
import ru.yandex.practicum.telemetry.collector.handler.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.handler.sensors.SensorEventHandler;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class CollectorController extends CollectorControllerGrpc.CollectorControllerImplBase implements AutoCloseable  {

    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlerMap;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlerMap;
    private final Producer<String, SpecificRecordBase> producer;

    public CollectorController(Set<SensorEventHandler> sensorEventHandlerSet,
                               Set<HubEventHandler> hubEventHandlerSet) {
        this.sensorEventHandlerMap = sensorEventHandlerSet.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageType,
                        Function.identity()
                ));
        this.hubEventHandlerMap = hubEventHandlerSet.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageType,
                        Function.identity()
                ));
        this.producer = initProducer();
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.debug("collectSensorEvent request = {}", request);
            if (sensorEventHandlerMap.containsKey(request.getPayloadCase())) {
                sensorEventHandlerMap.get(request.getPayloadCase()).handle(request, producer);
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.debug("collectHubEvent request = {}", request);
            if (hubEventHandlerMap.containsKey(request.getPayloadCase())) {
                hubEventHandlerMap.get(request.getPayloadCase()).handle(request, producer);
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    private Producer<String, SpecificRecordBase> initProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "serializer.AvroSerializer");

        return new KafkaProducer<>(config);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}
