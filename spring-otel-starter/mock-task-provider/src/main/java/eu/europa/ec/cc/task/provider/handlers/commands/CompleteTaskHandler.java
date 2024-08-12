package eu.europa.ec.cc.task.provider.handlers.commands;

import eu.europa.ec.cc.model.taskprovider.commands.CompleteTask;
import eu.europa.ec.cc.model.taskprovider.events.TaskCompleted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompleteTaskHandler {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @EventListener
    public void handleCompleteTask(CompleteTask command) {
        log.info("Complete task: {}", command);

        kafkaTemplate.send("cc_docker_event", TaskCompleted.builder()
                    .taskId(command.getTaskId())
                    .userId(command.getUserId())
                    .build());
    }

}
