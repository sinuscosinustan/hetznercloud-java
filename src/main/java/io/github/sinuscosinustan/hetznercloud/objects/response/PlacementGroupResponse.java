package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.PlacementGroup;

@Data
public class PlacementGroupResponse {

    @JsonProperty("placement_group")
    private PlacementGroup placementGroup;

}
