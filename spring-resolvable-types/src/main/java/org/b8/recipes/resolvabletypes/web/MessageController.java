package org.b8.recipes.resolvabletypes.web;

import org.b8.recipes.resolvabletypes.model.command.ClaimTask;
import org.b8.recipes.resolvabletypes.model.command.CompleteTask;
import org.b8.recipes.resolvabletypes.model.event.TaskClaimed;
import org.b8.recipes.resolvabletypes.model.event.TaskCompleted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/messages")
@RestController
public class MessageController {

    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/ClaimTask")
    public void postClaimTask() {
        kafkaTemplate.send("commands", ClaimTask.builder()
                    .taskInstanceId("666")
                    .userId("bastege")
                    .build());
    }

    @PostMapping("/CompleteTask")
    public void postCompleteTask() {
        kafkaTemplate.send("commands", CompleteTask.builder()
                    .taskInstanceId("667")
                    .outcome("accept")
                    .build());
    }

    @PostMapping("/TaskClaimed")
    public void postTaskClaimed() {
        kafkaTemplate.send("events", TaskClaimed.builder()
                    .taskInstanceId("669")
                    .userId("simonwa")
                    .build());
    }

    @PostMapping("/TaskCompleted")
    public void postTaskCompleted() {
        kafkaTemplate.send("events", TaskCompleted.builder()
                    .taskInstanceId("668")
                    .outcome("rejected")
                    .build());
    }

}
