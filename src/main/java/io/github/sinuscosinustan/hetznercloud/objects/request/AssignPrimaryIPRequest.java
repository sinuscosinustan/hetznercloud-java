package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPAssigneeType;

@Data
@Builder
@AllArgsConstructor
public class AssignPrimaryIPRequest {

    @JsonProperty("assignee_id")
    private Long assigneeId;
    @JsonProperty("assignee_type")
    private IPAssigneeType assigneeType;
}