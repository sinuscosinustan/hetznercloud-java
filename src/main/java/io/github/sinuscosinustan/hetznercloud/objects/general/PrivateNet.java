package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PrivateNet {

    private Long network;
    private String ip;
    @JsonProperty("alias_ip")
    private List<String> aliasIps;
    @JsonProperty("mac_address")
    private String macAddress;
}
