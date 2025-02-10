package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LBHealthStatus {

    @JsonProperty("listen_port")
    private Long listenPort;
    private String status;

}
