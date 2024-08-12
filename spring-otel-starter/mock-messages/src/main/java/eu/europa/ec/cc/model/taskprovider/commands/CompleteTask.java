package eu.europa.ec.cc.model.taskprovider.commands;

import eu.europa.ec.cc.model.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTask implements Command {

    private String taskId;
    private String userId;

}
