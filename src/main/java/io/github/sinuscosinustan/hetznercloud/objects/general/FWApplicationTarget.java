package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.github.sinuscosinustan.hetznercloud.objects.enums.TargetType;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FWApplicationTarget {

    @JsonProperty("label_selector")
    private FWLabelSelector labelSelector;
    private FWServerRef server;
    private TargetType type;

}
