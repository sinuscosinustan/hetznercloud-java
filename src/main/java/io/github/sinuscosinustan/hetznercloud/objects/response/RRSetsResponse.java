package io.github.sinuscosinustan.hetznercloud.objects.response;

import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.RRSet;
import lombok.Data;

import java.util.List;

@Data
public class RRSetsResponse {

    private List<RRSet> rrsets;

    private Meta meta;
}