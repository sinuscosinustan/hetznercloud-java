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
public class ServerPublicNetRequest {

    @JsonProperty("enable_ipv4")
    private Boolean enableIPv4;
    @JsonProperty("enable_ipv6")
    private Boolean enableIPv6;
    private Long ipv4;
    private Long ipv6;
}