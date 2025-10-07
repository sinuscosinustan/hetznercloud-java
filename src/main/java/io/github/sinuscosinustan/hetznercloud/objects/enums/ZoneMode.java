package io.github.sinuscosinustan.hetznercloud.objects.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ZoneMode {
    PRIMARY("primary"),
    SECONDARY("secondary");

    @JsonValue
    private final String value;
}