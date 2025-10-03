package io.github.sinuscosinustan.hetznercloud.objects.general;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.Architecture;
import io.github.sinuscosinustan.hetznercloud.objects.pricing.LocationPrice;

@Data
public class ServerType {

    private Long id;
    private String name;
    private String description;
    private Long cores;
    private Long memory;
    private Long disk;

    /**
     * @deprecated Use the per-location deprecation information in the locations array instead.
     * This field will be gradually phased out as per-location deprecations are being announced.
     */
    @Deprecated
    private Boolean deprecated;

    /**
     * @deprecated Use the per-location deprecation information in the locations array instead.
     * This field will be gradually phased out as per-location deprecations are being announced.
     */
    @Deprecated
    private Deprecation deprecation;

    private List<LocationPrice> prices;
    @JsonProperty("storage_type")
    private String storageType;
    @JsonProperty("cpu_type")
    private String cpuType;
    private Architecture architecture;
    @JsonProperty("included_traffic")
    private Long includedTraffic;
    private String category;
    private List<ServerTypeLocation> locations;
}
