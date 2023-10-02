package org.b8.recipes.resolvabletypes.handler.command;

import lombok.extern.slf4j.Slf4j;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaCommand;
import org.b8.recipes.resolvabletypes.model.command.ClaimTask;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClaimTaskHandler {

    @EventListener
    public void handle(KafkaCommand<ClaimTask> claimTaskCommand) {
        log.info("Handling {}", claimTaskCommand.getValue());
    }

}
