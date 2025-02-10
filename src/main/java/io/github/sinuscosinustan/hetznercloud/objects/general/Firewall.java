package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firewall {

    @JsonProperty("applied_to")
    private List<FWApplicationTarget> appliedTo;
    @JsonDeserialize(contentUsing = DateDeserializer.class)
    private Date created;
    private Long id;
    private Map<String, String> labels;
    private String name;
    @JsonProperty("rules")
    private List<FirewallRule> firewallRules;

}
