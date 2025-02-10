package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.Network;

import java.util.List;

@Data
public class NetworksResponse {

    private List<Network> networks;
    private Meta meta;
}
