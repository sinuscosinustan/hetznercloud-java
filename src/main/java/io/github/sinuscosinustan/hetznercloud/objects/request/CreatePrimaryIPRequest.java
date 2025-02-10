package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPAssigneeType;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPType;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePrimaryIPRequest {

    @JsonProperty("assignee_id")
    private Long assigneeId;
    @JsonProperty("assignee_type")
    private IPAssigneeType assigneeType;
    @JsonProperty("auto_delete")
    private Boolean autoDelete;
    private String datacenter;
    @Singular
    private Map<String, String> labels;
    private String name;
    private IPType type;
}