package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import io.github.sinuscosinustan.hetznercloud.objects.general.LBHealthCheck;
import io.github.sinuscosinustan.hetznercloud.objects.general.LBServiceHttp;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LBServiceRequest {

    @NonNull
    private String protocol;
    @NonNull
    @JsonProperty("listen_port")
    private Long listenPort;
    @NonNull
    @JsonProperty("destination_port")
    private Long destinationPort;
    private Boolean proxyprotocol;
    @JsonProperty("health_check")
    private LBHealthCheck healthCheck;
    private LBServiceHttp http;

}
