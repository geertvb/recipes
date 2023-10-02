package org.b8.recipes.resolvabletypes.handler.event;

import lombok.extern.slf4j.Slf4j;
import org.b8.recipes.resolvabletypes.kafka.model.KafkaEvent;
import org.b8.recipes.resolvabletypes.model.event.TaskClaimed;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskClaimedHandler {

    @EventListener
    public void handle(KafkaEvent<TaskClaimed> taskClaimedEvent) {
        log.info("Handling {}", taskClaimedEvent.getValue());
    }

}
