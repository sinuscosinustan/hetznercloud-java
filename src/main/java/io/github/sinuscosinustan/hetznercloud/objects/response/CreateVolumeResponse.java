package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.Volume;

import java.util.List;
import java.util.Map;

@Data
public class CreateVolumeResponse {

    private Volume volume;
    private Action action;
    @JsonProperty("next_actions")
    private List<Action> nextActions;
    private Map<String, String> labels;
}
