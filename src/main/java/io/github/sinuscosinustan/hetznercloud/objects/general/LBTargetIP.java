package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LBTargetIP {

    private String ip;
}
