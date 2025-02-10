package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Location {

    private Long id;
    private String name;
    private String description;
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;
    @JsonProperty("network_zone")
    private String networkZone;
}
