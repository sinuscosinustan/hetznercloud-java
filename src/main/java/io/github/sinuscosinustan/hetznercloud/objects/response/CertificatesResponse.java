package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Certificate;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;

import java.util.List;

@Data
public class CertificatesResponse {

    private List<Certificate> certificates;
    private Meta meta;
}
