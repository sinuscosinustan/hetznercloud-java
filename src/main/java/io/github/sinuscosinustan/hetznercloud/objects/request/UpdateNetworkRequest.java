package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateNetworkRequest {

    private String name;
    @Singular
    private Map<String, String> labels;
    @JsonProperty("expose_routes_to_vswitch")
    private Boolean exposeRoutesToVswitch;
}
