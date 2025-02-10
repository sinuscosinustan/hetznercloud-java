package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Datacenter;

import java.util.List;

@Data
public class DatacentersResponse {

    private List<Datacenter> datacenters;
    private Long recommendation;
}
