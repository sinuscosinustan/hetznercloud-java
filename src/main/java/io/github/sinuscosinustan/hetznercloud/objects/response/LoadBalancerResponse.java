package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.LoadBalancer;

@Data
public class LoadBalancerResponse {

    @JsonProperty("load_balancer")
    private LoadBalancer loadBalancer;
}
