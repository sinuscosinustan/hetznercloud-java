package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.PrimaryIP;

@Data
public class CreatePrimaryIPResponse {

    private Action action;
    @JsonProperty("primary_ip")
    private PrimaryIP primaryIP;
}
