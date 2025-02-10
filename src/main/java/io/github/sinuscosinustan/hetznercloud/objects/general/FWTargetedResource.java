package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.sinuscosinustan.hetznercloud.objects.enums.TargetType;

@Data
@NoArgsConstructor
public class FWTargetedResource {

    private FWServerRef server;
    private TargetType type = TargetType.server;

    public FWTargetedResource(FWServerRef serverRef) {
        this.server = serverRef;
    }

    public FWTargetedResource(Long serverId) {
        this.server = new FWServerRef(serverId);
    }

    public FWTargetedResource(Server server) {
        this.server = new FWServerRef(server.getId());
    }

}
