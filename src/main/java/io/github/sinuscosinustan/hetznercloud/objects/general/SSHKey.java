package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class SSHKey {

    private Long id;
    private String name;
    private String fingerprint;
    @JsonProperty("public_key")
    private String publicKey;
    private Map<String, String> labels;
}
