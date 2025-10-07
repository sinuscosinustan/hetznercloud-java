package io.github.sinuscosinustan.hetznercloud.objects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum APIType {
    /**
     * Hetzner Cloud API (default) - for cloud resources like servers, networks, etc.
     */
    CLOUD("https://api.hetzner.cloud/v1"),

    /**
     * Hetzner Online API - At this time only for Storage boxes
     */
    HETZNER_ONLINE("https://api.hetzner.com/v1");

    private final String baseUrl;
}