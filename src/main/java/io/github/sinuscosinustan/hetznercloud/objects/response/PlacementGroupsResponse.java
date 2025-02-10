package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.PlacementGroup;

import java.util.List;

@Data
public class PlacementGroupsResponse {

    @JsonProperty("placement_groups")
    private List<PlacementGroup> placementGroups;
    private Meta meta;
}
