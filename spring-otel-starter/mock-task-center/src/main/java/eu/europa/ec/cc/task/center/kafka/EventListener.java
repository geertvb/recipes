package eu.europa.ec.cc.task.center.kafka;

import eu.europa.ec.cc.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @KafkaListener(topics = "cc_docker_event")
    public void listen(Event event) {
        applicationEventPublisher.publishEvent(event);
    }

}
