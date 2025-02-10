package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.Architecture;

@Data
public class ISO {

    private Long id;
    private String name;
    private String description;
    private String type;
    private Meta meta;
    private Architecture architecture;
    private Deprecation deprecation;

}
