package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.PrimaryIP;

import java.util.List;

@Data
public class PrimaryIPsResponse {

    @JsonProperty("primary_ips")
    private List<PrimaryIP> primaryIPs;
    private Meta meta;
}
