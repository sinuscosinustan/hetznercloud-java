package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import io.github.sinuscosinustan.hetznercloud.objects.general.LBService;
import io.github.sinuscosinustan.hetznercloud.objects.general.LBTarget;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateLoadBalancerRequest {

    private String name;
    @JsonProperty("load_balancer_type")
    private String loadBalancerType;
    @JsonProperty("network_zone")
    private String networkZone;
    private String location;
    private CreateLoadBalancerRequestAlgorithmType algorithm;
    private List<LBService> services;
    private List<LBTarget> targets;
    @Singular
    private Map<String, String> labels;
    @JsonProperty("public_interface")
    private Boolean publicInterface;
    private Long network;
}
