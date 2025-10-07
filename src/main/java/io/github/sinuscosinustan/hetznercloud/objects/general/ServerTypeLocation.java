package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;

@Data
public class ServerTypeLocation {

    private Long id;
    private String name;
    private Deprecation deprecation;

}