package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class FloatingIP {

    private Long id;
    private String name;
    private String description;
    private String ip;
    private IPType type;
    private Long server;
    @JsonProperty("dns_ptr")
    private List<DnsPTR> dnsPTR;
    @JsonProperty("home_location")
    private Location homeLocation;
    private Boolean blocked;
    private Protection protection;
    private Map<String, String> labels;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
}