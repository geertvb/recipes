package eu.europa.ec.cc.model.taskcenter.commands;

import eu.europa.ec.cc.model.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCompleteTask implements Command {
    private String taskId;
    private String userId;
}
