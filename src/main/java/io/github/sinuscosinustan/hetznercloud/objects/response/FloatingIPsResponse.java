package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.FloatingIP;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class FloatingIPsResponse {

    @JsonProperty("floating_ips")
    private List<FloatingIP> floatingIps;
    private Meta meta;
}
