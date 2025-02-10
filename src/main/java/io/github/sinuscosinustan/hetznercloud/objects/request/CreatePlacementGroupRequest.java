package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import io.github.sinuscosinustan.hetznercloud.objects.enums.PlacementGroupType;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePlacementGroupRequest {

    private String name;
    private PlacementGroupType type;
    @Singular
    private Map<String, String> labels;
}
