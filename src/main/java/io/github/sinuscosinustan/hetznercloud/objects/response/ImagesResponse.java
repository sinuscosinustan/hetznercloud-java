package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Image;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class ImagesResponse {

    private List<Image> images;
    private Meta meta;
}
