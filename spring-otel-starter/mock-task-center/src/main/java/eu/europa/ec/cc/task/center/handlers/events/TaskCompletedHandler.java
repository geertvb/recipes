package eu.europa.ec.cc.task.center.handlers.events;

import eu.europa.ec.cc.model.taskcenter.events.CompleteTaskConfirmed;
import eu.europa.ec.cc.model.taskprovider.events.TaskCompleted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskCompletedHandler {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @EventListener
    public void handleTaskCompleted(TaskCompleted event) {
        log.info("TaskCompleted: {}", event);

        kafkaTemplate.send("cc_docker_event", CompleteTaskConfirmed.builder()
                    .taskId(event.getTaskId())
                    .userId(event.getUserId())
                    .build());
    }

}
