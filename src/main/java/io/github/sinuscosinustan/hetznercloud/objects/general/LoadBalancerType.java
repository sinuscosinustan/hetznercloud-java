package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;
import io.github.sinuscosinustan.hetznercloud.objects.pricing.LocationPrice;

import java.util.Date;
import java.util.List;

@Data
public class LoadBalancerType {

    private Long id;
    private String name;
    private String description;
    @JsonProperty("max_connections")
    private Long maxConnections;
    @JsonProperty("max_services")
    private Long maxServices;
    @JsonProperty("max_targets")
    private Long maxTargets;
    @JsonProperty("max_assigned_certificates")
    private Long maxAssignedCertificates;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date deprecated;
    private List<LocationPrice> prices;
}