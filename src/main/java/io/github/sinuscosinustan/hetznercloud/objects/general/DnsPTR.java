package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DnsPTR {

    @JsonProperty("dns_ptr")
    private String dnsPTR;
    private String ip;
}
