package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ZoneMode;
import io.github.sinuscosinustan.hetznercloud.objects.general.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateZoneRequest {

    @NonNull
    private String name;

    private Long ttl;

    private ZoneMode mode;

    @JsonProperty("primary_nameservers")
    private List<Zone.PrimaryNameserver> primaryNameservers;

    private Map<String, String> labels;
}