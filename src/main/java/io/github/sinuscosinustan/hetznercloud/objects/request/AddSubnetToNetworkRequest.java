package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddSubnetToNetworkRequest {

    private String type;
    @JsonProperty("ip_range")
    private String ipRange;
    @JsonProperty("network_zone")
    private String networkZone;
    @JsonProperty("vswitch_id")
    private Long vSwitchId;
}
