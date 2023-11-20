package org.b8.kafka.producer;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static java.lang.Long.toHexString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class CompleteTaskApp {

    private final Properties producerProperties = producerProperties();

    private final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(producerProperties);

    private final Random random = new Random();

    private final Supplier<String> traceIdSupplier = () -> toHexString(random.nextLong());

    private final Supplier<String> idSupplier = () -> UUID.randomUUID().toString();

    private Properties producerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    protected Header header(String key, String value) {
        return new RecordHeader(key, value == null ? null : value.getBytes(UTF_8));
    }

    protected ProducerRecord<String, String> producerRecord(
                String topicName, String key, String message, String producerName, String traceId) {

        var value = "{"
                    + "\"message\":\"" + message + "\","
                    + "\"key\":\"" + key + "\""
                    + "}";

        return new ProducerRecord<>(
                    topicName,
                    null,
                    null,
                    key,
                    value,
                    List.of(
                                header("X-B3-TraceId", traceId),
                                header("application", producerName),
                                header("producer.host.name", producerName),
                                header("__typeId__", message)
                    ));
    }

    @SneakyThrows
    protected void wait(int minMillis, int maxMillis) {
        var millis = minMillis + random.nextInt(maxMillis - minMillis);
        Thread.sleep(millis);
    }

    public String completeTask() {
        var taskId = idSupplier.get();
        var traceId = traceIdSupplier.get();

        kafkaProducer.send(producerRecord("test-command-task-provider", taskId, "ClaimTask", "task-details", traceId));
        wait(100, 1000);
        kafkaProducer.send(producerRecord("test-command-task-allocation", taskId, "ResolveAllocation", "task-provider", traceId));
        wait(10, 100);
        kafkaProducer.send(producerRecord("test-event", taskId, "AllocationResolved", "task-allocation", traceId));
        wait(100, 2000);
        kafkaProducer.send(producerRecord("test-event", taskId, "TaskClaimed", "task-provider", traceId));

        return "";
    }

    public void run() throws Exception {
        var callables = new ArrayList<Callable<String>>();
        for (int i = 0; i < 10; i++) {
            callables.add(this::completeTask);
        }

        ExecutorService executorService = newFixedThreadPool(callables.size());
        executorService.invokeAll(callables);
        executorService.shutdown();
    }

    public static void main(String... args) throws Exception {
        new CompleteTaskApp().run();
    }

}
