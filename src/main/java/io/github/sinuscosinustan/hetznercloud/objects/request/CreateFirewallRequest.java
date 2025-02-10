package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import io.github.sinuscosinustan.hetznercloud.objects.general.FWApplicationTarget;
import io.github.sinuscosinustan.hetznercloud.objects.general.FirewallRule;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CreateFirewallRequest {

    @Singular("applicationTarget")
    @JsonProperty("apply_to")
    private List<FWApplicationTarget> applyTo;
    @Singular
    private Map<String, String> labels;
    private String name;
    @Singular("firewallRule")
    @JsonProperty("rules")
    private List<FirewallRule> firewallRules;

}
