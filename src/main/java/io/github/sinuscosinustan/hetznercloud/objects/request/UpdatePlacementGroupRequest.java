package io.github.sinuscosinustan.hetznercloud.objects.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class UpdatePlacementGroupRequest {

    private String name;
    private Map<String, String> labels;

}
