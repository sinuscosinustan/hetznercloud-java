package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ZoneMode;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Zone {

    private Long id;
    private String name;
    private Long ttl;
    private ZoneMode mode;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;

    @JsonProperty("primary_nameservers")
    private List<PrimaryNameserver> primaryNameservers;

    @JsonProperty("protection")
    private Protection protection;

    private Map<String, String> labels;

    private String status;

    @JsonProperty("record_count")
    private Integer recordCount;

    @JsonProperty("authoritative_nameservers")
    private AuthoritativeNameservers authoritativeNameservers;

    private String registrar;

    @Data
    public static class PrimaryNameserver {
        private String address;
        private Integer port;
        @JsonProperty("tsig_key")
        private String tsigKey;
        @JsonProperty("tsig_algorithm")
        private String tsigAlgorithm;
    }

    @Data
    public static class AuthoritativeNameservers {
        private List<String> assigned;
        private List<String> delegated;
        @JsonProperty("delegation_last_check")
        @JsonDeserialize(using = DateDeserializer.class)
        private Date delegationLastCheck;
        @JsonProperty("delegation_status")
        private String delegationStatus;
    }
}
