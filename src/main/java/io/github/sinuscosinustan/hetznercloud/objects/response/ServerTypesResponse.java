package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.ServerType;

import java.util.List;

@Data
public class ServerTypesResponse {

    @JsonProperty("server_types")
    private List<ServerType> serverTypes;

}