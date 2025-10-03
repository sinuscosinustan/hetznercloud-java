package io.github.sinuscosinustan.hetznercloud.objects.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RRSetType {
    A("A"),
    AAAA("AAAA"),
    CAA("CAA"),
    CNAME("CNAME"),
    DS("DS"),
    HINFO("HINFO"),
    HTTPS("HTTPS"),
    MX("MX"),
    NS("NS"),
    PTR("PTR"),
    RP("RP"),
    SOA("SOA"),
    SRV("SRV"),
    SVCB("SVCB"),
    TLSA("TLSA"),
    TXT("TXT");

    @JsonValue
    private final String value;
}