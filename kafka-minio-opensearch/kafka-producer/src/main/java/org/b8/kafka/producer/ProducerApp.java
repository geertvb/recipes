package org.b8.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ProducerApp {

    public static void main(String... args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (var producer = new KafkaProducer<String, String>(props)) {
            for (int i = 0; i < 100; i++) {
                var record = new ProducerRecord<>(
                        "test-topic",
                        null,
                        null,
                        "key-" + i,
                        "{\"value\":\"" + i +"\"}",
                        List.of(
                                new RecordHeader("traceId", UUID.randomUUID().toString().getBytes()),
                                new RecordHeader("application", "kafka-producer".getBytes())
                        ));
                producer.send(record).get(5, SECONDS);
            }
        }
    }

}
