package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.Volume;

import java.util.List;

@Data
public class VolumesResponse {

    private List<Volume> volumes;
    private Meta meta;

}