package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.sinuscosinustan.hetznercloud.objects.general.Firewall;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirewallsResponse {

    private List<Firewall> firewalls;
    private Meta meta;

}
