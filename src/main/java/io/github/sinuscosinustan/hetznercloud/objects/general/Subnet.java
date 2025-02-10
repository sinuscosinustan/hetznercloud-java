package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.SubnetType;

@Data
public class Subnet {

    private SubnetType type;
    @JsonProperty("ip_range")
    private String ipRange;
    @JsonProperty("network_zone")
    private String networkZone;
    private String gateway;
}
