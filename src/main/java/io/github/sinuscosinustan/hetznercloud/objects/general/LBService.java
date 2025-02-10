package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LBService {

    private String protocol;
    @JsonProperty("listen_port")
    private Long listenPort;
    @JsonProperty("destination_port")
    private Long destinationPort;
    private Boolean proxyprotocol;
    @JsonProperty("health_check")
    private LBHealthCheck healthCheck;
    private LBServiceHttp http;

}
