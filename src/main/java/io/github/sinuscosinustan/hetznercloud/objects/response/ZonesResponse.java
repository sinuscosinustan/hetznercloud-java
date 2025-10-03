package io.github.sinuscosinustan.hetznercloud.objects.response;

import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.Zone;
import lombok.Data;

import java.util.List;

@Data
public class ZonesResponse {

    private List<Zone> zones;

    private Meta meta;
}