package eu.europa.ec.cc.task.center.kafka;

import eu.europa.ec.cc.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CommandListener {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @KafkaListener(topics = "cc_docker_command-task-center")
    public void listen(Command command) {
        applicationEventPublisher.publishEvent(command);
    }

}
