package io.github.sinuscosinustan.hetznercloud.objects.response;

import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.Zone;
import lombok.Data;

import java.util.List;

@Data
public class ZoneResponse {

    private Zone zone;

    private List<Action> actions;
}