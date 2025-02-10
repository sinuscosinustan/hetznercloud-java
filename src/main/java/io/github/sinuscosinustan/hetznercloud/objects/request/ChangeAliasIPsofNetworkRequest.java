package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChangeAliasIPsofNetworkRequest {

    private Long network;
    @JsonProperty("alias_ips")
    private List<String> aliasIps;
}
