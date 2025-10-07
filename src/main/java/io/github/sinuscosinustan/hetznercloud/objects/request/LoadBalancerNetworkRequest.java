package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadBalancerNetworkRequest {

    @JsonProperty("network")
    @NonNull private final Long network;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("ip_range")
    private String ipRange;

    /**
     * Constructor for backward compatibility
     * @param network Network ID
     * @param ip IP address
     */
    public LoadBalancerNetworkRequest(Long network, String ip) {
        this.network = network;
        this.ip = ip;
    }
}
