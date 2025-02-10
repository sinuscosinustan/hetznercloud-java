package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CreateSSHKeyRequest {

    private String name;

    @JsonProperty("public_key")
    private String publicKey;

    @Singular
    private Map<String, String> labels;

}
