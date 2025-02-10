package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.SSHKey;

import java.util.List;

@Data
public class SSHKeysResponse {

    @JsonProperty("ssh_keys")
    private List<SSHKey> sshKeys;
    private Meta meta;
}