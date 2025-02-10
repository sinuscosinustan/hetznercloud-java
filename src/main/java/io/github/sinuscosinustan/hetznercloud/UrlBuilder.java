package io.github.sinuscosinustan.hetznercloud;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Helper for easier URL building
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class UrlBuilder {
    private final String url;

    private Map<String, String> queryParams = new HashMap<>();

    /**
     * Creates a new {@link UrlBuilder} instance
     *
     * @param url base url (must not be blank/null)
     * @return the instance
     */
    public static UrlBuilder from(String url) {
        if (url == null || url.isBlank()) {
            throw new RuntimeException("url must not be empty or blank");
        }

        return new UrlBuilder(url);
    }

    /**
     * Add a new query parameter or overwrites an existing one with the same name if the value exists
     *
     * @param name  query parameter name
     * @param value optional query parameter value
     * @return the current {@link UrlBuilder} instance
     */
    public UrlBuilder queryParamIfPresent(String name, Optional<Object> value) {
        value.ifPresent((it) -> queryParams.put(name, it.toString()));
        return this;
    }

    /**
     * Render full url as URI string
     *
     * @return the uri string
     */
    public String toUri() {
        if (queryParams.isEmpty()) {
            return url;
        }

        return url + "?" + queryParams.entrySet().stream().map((it) -> it.getKey() + "=" + it.getValue()).collect(Collectors.joining("&"));
    }

    /**
     * Add a new query parameter or overwrites an existing one with the same name
     *
     * @param name  query parameter name
     * @param value query parameter value
     * @return the current {@link UrlBuilder} instance
     */
    public UrlBuilder queryParam(String name, Object value) {
        queryParams.put(name, Objects.toString(value));
        return this;
    }
}
