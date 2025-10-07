package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class LoadBalancer {

    private Long id;
    private String name;
    private Boolean publicEnabled;
    private String publicIpv4;
    private String publicIpv6;
    @JsonProperty("private_net")
    private List<LBPrivateNet> privateNet;
    private Location location;
    @JsonProperty("load_balancer_type")
    private LoadBalancerType loadBalancerType;
    private Protection protection;
    private Map<String, String> labels;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    private List<LBService> services;
    private List<LBTarget> targets;
    private String algorithmType;

    @JsonProperty("algorithm")
    private void algorithmDeserializer(Map<String, Object> algorithm) {
        this.algorithmType = (String) algorithm.get("type");
    }

    @JsonProperty("public_net")
    private void publicNetDeserializer(Map<String, Object> publicNet) {
        this.publicEnabled = (Boolean) publicNet.get("enabled");
        this.publicIpv4 = (String) ((Map) publicNet.get("ipv4")).get("ip");
        this.publicIpv6 = (String) ((Map) publicNet.get("ipv6")).get("ip");
    }

    @Data
    public static class LBPrivateNet {
        private Long network;
        private String ip;
    }
}
