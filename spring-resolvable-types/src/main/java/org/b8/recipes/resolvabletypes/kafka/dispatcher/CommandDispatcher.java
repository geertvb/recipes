package org.b8.recipes.resolvabletypes.kafka.dispatcher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaCommand;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaMessage;
import org.b8.recipes.resolvabletypes.model.command.ClaimTask;
import org.b8.recipes.resolvabletypes.model.command.CompleteTask;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommandDispatcher extends AbstractKafkaDispatcher {

    @Getter
    protected List<Class> supportedTypes = List.of(
                CompleteTask.class,
                ClaimTask.class);

    @Override
    public KafkaMessage createKafkaMessage() {
        return new KafkaCommand();
    }

    @Override
    @KafkaListener(topics = "commands")
    public void commandHandler(ConsumerRecord<String, Object> consumerRecord) {
        super.commandHandler(consumerRecord);
    }

}
