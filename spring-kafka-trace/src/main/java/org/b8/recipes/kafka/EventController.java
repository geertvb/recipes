package org.b8.recipes.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class EventController {

    @Autowired
    protected Supplier<String> idSupplier;

    @Autowired
    protected KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping(consumes = "text/plain")
    public void postEvent(@RequestBody String event) {
        log.info("Posting new event: {}", event);

        String key = idSupplier.get();
        kafkaTemplate.send("event", key, event);
    }

}
