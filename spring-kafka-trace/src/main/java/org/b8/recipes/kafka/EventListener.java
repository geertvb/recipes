package org.b8.recipes.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class EventListener {

    @Autowired
    protected Supplier<String> idSupplier;

    @Autowired
    protected KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "event")
    public void handleEvent(ConsumerRecord<String, String> consumerRecord) {
        log.info("Handle event: {}", consumerRecord);

        String key = idSupplier.get();
        String command = "command <- " + consumerRecord.value();
        kafkaTemplate.send("command", key, command);
    }

}
