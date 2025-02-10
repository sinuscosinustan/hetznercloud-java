package io.github.sinuscosinustan.hetznercloud;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class UrlBuilderTest {

    @Test
    void testNullUrl() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            UrlBuilder.from(null);
        });
    }

    @Test
    void testEmptyUrl() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            UrlBuilder.from("");
        });

        Assertions.assertThrows(RuntimeException.class, () -> {
            UrlBuilder.from("  ");
        });
    }

    @Test
    void testOnlyUrl() {
        assertThat(UrlBuilder.from("http://www.heise.de").toUri()).isEqualTo("http://www.heise.de");
    }

    @Test
    void testUrlSingleQueryParam() {
        assertThat(UrlBuilder.from("http://www.heise.de").queryParam("key1", "value1").toUri()).isEqualTo("http://www.heise.de?key1=value1");
    }

    @Test
    void testUrlMultipleQueryParams() {
        assertThat(UrlBuilder.from("http://www.heise.de")
                .queryParam("key1", "value1")
                .queryParam("key2", "value2")
                .toUri()).isEqualTo("http://www.heise.de?key1=value1&key2=value2");
    }

    @Test
    void testUrlMultipleQueryParamTypes() {
        assertThat(UrlBuilder.from("http://www.heise.de")
                .queryParam("key1", "value1")
                .queryParam("key2", 1)
                .queryParam("key3", true)
                .toUri()).isEqualTo("http://www.heise.de?key1=value1&key2=1&key3=true");
    }
}
