package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;

@Data
public class Route {

    private String destination;
    private String gateway;
}
