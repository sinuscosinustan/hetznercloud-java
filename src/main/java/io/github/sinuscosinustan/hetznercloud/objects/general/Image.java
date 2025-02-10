package io.github.sinuscosinustan.hetznercloud.objects.general;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.Singular;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;
import io.github.sinuscosinustan.hetznercloud.objects.enums.Architecture;

@Data
public class Image {

    private Long id;
    private String type;
    private String status;
    private String name;
    private String description;
    @JsonProperty("image_size")
    private Double imageSize;
    @JsonProperty("disk_size")
    private Double diskSize;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date deleted;
    @JsonProperty("created_from")
    private CreatedFrom createdFrom;
    @JsonProperty("bound_to")
    private Long boundTo;
    @JsonProperty("os_flavor")
    private String osFlavor;
    @JsonProperty("os_version")
    private String osVersion;
    @JsonProperty("rapid_redeploy")
    private boolean rapidRedeploy;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date deprecated;
    private Protection protection;
    @Singular
    private Map<String, String> labels;
    private Architecture architecture;

    @Data
    public static class CreatedFrom {
        private Long id;
        private String name;
    }

    @Data
    @Deprecated
    public static class Protect {
        private boolean delete;
    }
}
