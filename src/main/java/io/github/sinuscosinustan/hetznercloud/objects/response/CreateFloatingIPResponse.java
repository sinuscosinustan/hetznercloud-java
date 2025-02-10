package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.FloatingIP;

@Data
public class CreateFloatingIPResponse {

    @JsonProperty("floating_ip")
    private FloatingIP floatingIP;
    private Action action;
}
