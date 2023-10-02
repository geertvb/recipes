package org.b8.recipes.resolvabletypes.handler.command;

import lombok.extern.slf4j.Slf4j;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaCommand;
import org.b8.recipes.resolvabletypes.model.command.CompleteTask;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompleteTaskHandler {

    @EventListener
    public void handle(KafkaCommand<CompleteTask> completeTaskCommand) {
        log.info("Handling {}", completeTaskCommand.getValue());
    }

}
