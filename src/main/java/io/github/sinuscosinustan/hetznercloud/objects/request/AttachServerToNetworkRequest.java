package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachServerToNetworkRequest {

    private Long network;
    private String ip;
    @JsonProperty("alias_ips")
    private List<String> aliasIps;
    @JsonProperty("ip_range")
    private String ipRange;
}
