package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.Firewall;

import java.util.List;

@Data
public class CreateFirewallResponse {

    private List<Action> actions;
    private Firewall firewall;

}
