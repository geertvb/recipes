package eu.europa.ec.cc.model.taskcenter.events;

import eu.europa.ec.cc.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskConfirmed implements Event {
    private String taskId;
    private String userId;
}
