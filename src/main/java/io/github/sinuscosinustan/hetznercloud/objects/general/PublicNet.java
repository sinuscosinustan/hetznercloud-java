package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PublicNet {

    private IPv4 ipv4;
    private IPv6 ipv6;
    @JsonProperty("floating_ips")
    private List<Long> floatingIPs;
    private List<Firewall> firewalls;

    @Data
    public static class IPv4 {
        private Long id;
        private String ip;
        private boolean blocked;
        @JsonProperty("dns_ptr")
        private String dnsPTR;
    }

    @Data
    public static class IPv6 {
        private Long id;
        private String ip;
        private boolean blocked;
        @JsonProperty("dns_ptr")
        private List<DnsPTR> dnsPTR;
    }

    @Data
    public static class Firewall {
        private Long id;
        private String status;
    }
}
