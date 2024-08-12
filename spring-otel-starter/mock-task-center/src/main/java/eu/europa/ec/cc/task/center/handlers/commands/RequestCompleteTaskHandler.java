package eu.europa.ec.cc.task.center.handlers.commands;

import eu.europa.ec.cc.model.taskcenter.commands.RequestCompleteTask;
import eu.europa.ec.cc.model.taskprovider.commands.CompleteTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestCompleteTaskHandler {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @EventListener
    public void handleRequestCompleteTask(RequestCompleteTask command) {
        log.info("Requesting complete task: {}", command);

        kafkaTemplate.send("cc_docker_command-task-provider", CompleteTask.builder()
                    .taskId(command.getTaskId())
                    .userId(command.getUserId())
                    .build());
    }

}
