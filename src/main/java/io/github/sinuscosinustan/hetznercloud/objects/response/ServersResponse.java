package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.Server;

import java.util.List;

@Data
public class ServersResponse {

    private List<Server> servers;
    private Meta meta;
}
