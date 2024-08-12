package eu.europa.ec.cc.task.center.web;

import eu.europa.ec.cc.model.taskcenter.commands.RequestCompleteTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @PostMapping(path = "/{taskId}/complete")
    public void complete(@RequestBody RequestCompleteTask requestCompleteTask) {
        kafkaTemplate.send("cc_docker_command-task-center", requestCompleteTask);
    }

}
