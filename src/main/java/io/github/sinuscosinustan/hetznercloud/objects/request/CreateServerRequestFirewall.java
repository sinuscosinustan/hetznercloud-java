package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class CreateServerRequestFirewall {

    @JsonProperty(value = "firewall")
    private Long firewallId;

}
