package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Location;

import java.util.List;

@Data
public class LocationsResponse {

    private List<Location> locations;
}
