package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.Certificate;

@Data
public class CertificateResponse {

    private Certificate certificate;
}
