package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.SSHKey;

@Data
public class SSHKeyResponse {

    @JsonProperty("ssh_key")
    private SSHKey sshKey;
}
