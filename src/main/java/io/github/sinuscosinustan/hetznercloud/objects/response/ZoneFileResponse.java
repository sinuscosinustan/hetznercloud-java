package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZoneFileResponse {

    @JsonProperty("zone_file")
    private String zoneFile;
}