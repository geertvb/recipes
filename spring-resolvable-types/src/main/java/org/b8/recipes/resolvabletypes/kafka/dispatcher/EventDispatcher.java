package org.b8.recipes.resolvabletypes.kafka.dispatcher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaEvent;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaMessage;
import org.b8.recipes.resolvabletypes.model.event.TaskClaimed;
import org.b8.recipes.resolvabletypes.model.event.TaskCompleted;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EventDispatcher extends AbstractKafkaDispatcher {

    @Getter
    protected List<Class> supportedTypes = List.of(
                TaskCompleted.class,
                TaskClaimed.class);

    @Override
    public KafkaMessage createKafkaMessage() {
        return new KafkaEvent();
    }

    @Override
    @KafkaListener(topics = "events")
    public void commandHandler(ConsumerRecord<String, Object> consumerRecord) {
        super.commandHandler(consumerRecord);
    }

}
