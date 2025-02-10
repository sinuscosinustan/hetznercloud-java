package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import io.github.sinuscosinustan.hetznercloud.objects.enums.CertificateType;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCertificateRequest {

    private String name;
    private String certificate;
    @JsonProperty("domain_names")
    private List<String> domainNames;
    @JsonProperty("private_key")
    private String privateKey;
    @Singular
    private Map<String, String> labels;
    private CertificateType type;
}
