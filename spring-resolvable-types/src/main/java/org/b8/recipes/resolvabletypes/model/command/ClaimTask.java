package org.b8.recipes.resolvabletypes.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimTask {

    private String taskInstanceId;
    private String userId;

}
