package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ServerType;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeTypeRequest {

    @JsonProperty("upgrade_disk")
    private boolean upgradeDisk;
    @JsonProperty("server_type")
    private ServerType serverType;

}