package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Metrics;

@Data
public class MetricsResponse {

    private Metrics metrics;

}
