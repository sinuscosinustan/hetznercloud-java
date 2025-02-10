package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirewallRule {

    private String description;
    @Singular("desinationIP")
    @JsonProperty("destination_ips")
    private List<String> destinationIPs;
    private Direction direction;
    @Builder.Default
    private String port = "";
    private Protocol protocol;
    @Singular("sourceIP")
    @JsonProperty("source_ips")
    private List<String> sourceIPs;

    public enum Direction {
        in, out
    }

    public enum Protocol {
        tcp, udp, icmp, esp, gre
    }

}
