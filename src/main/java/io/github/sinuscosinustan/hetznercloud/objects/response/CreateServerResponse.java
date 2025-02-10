package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.Server;

import java.util.List;

@Data
public class CreateServerResponse {

    private Server server;
    private Action action;
    @JsonProperty("next_actions")
    private List<Action> nextActions;
    @JsonProperty("root_password")
    private String rootPassword;
}
