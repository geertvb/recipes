package org.b8.recipes.resolvabletypes.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskClaimed {

    private String taskInstanceId;
    private String userId;

}
