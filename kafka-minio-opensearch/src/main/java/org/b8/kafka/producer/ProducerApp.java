package org.b8.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ProducerApp {

    public static void main(String... args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (var producer = new KafkaProducer<String, String>(props)) {
            for (int i = 0; i < 100; i++) {
                var record = new ProducerRecord<>("test-topic", "key-" + i, "value-" + i);
                producer.send(record).get(5, SECONDS);
            }
        }
    }

}
