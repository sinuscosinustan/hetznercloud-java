package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.ServerType;

@Data
public class ServerTypeResponse {

    @JsonProperty("server_type")
    private ServerType serverType;
}
