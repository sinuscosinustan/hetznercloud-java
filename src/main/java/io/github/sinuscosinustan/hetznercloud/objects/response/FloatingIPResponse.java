package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.FloatingIP;

@Data
public class FloatingIPResponse {

    @JsonProperty("floating_ip")
    private FloatingIP floatingIP;
}
