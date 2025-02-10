package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Datacenter;

@Data
public class DatacenterResponse {

    private Datacenter datacenter;
}
