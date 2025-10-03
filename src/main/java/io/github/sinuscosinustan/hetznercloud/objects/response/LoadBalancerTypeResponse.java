package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.LoadBalancerType;

@Data
public class LoadBalancerTypeResponse {

    @JsonProperty("load_balancer_type")
    private LoadBalancerType loadBalancerType;

}
