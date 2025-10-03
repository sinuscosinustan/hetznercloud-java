package io.github.sinuscosinustan.hetznercloud.unit;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.enums.APIType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

class APIConfigurationTest {

    @Nested
    @DisplayName("API URL Configuration")
    class APIURLConfigurationTests {

        @Test
        @DisplayName("Should support APIType enum with correct URLs")
        void shouldSupportAPITypeEnumWithCorrectURLs() {
            assertThat(APIType.CLOUD.getBaseUrl()).isEqualTo("https://api.hetzner.cloud/v1");
            assertThat(APIType.HETZNER_ONLINE.getBaseUrl()).isEqualTo("https://api.hetzner.com/v1");
        }

        @Test
        @DisplayName("Should create API instances with different endpoint types")
        void shouldCreateAPIInstancesWithDifferentEndpointTypes() {
            HetznerCloudAPI cloudAPI = new HetznerCloudAPI("test-token", APIType.CLOUD);
            HetznerCloudAPI onlineAPI = new HetznerCloudAPI("test-token", APIType.HETZNER_ONLINE);
            HetznerCloudAPI customAPI = new HetznerCloudAPI("test-token", "https://custom.api.com/v1");

            assertThat(cloudAPI).isNotNull();
            assertThat(onlineAPI).isNotNull();
            assertThat(customAPI).isNotNull();
        }

        @Test
        @DisplayName("Should maintain backward compatibility with existing constructors")
        void shouldMaintainBackwardCompatibilityWithExistingConstructors() {
            // These should still work exactly as before
            HetznerCloudAPI defaultAPI = new HetznerCloudAPI("test-token");
            HetznerCloudAPI withClientAPI = new HetznerCloudAPI("test-token", new okhttp3.OkHttpClient());

            assertThat(defaultAPI).isNotNull();
            assertThat(withClientAPI).isNotNull();
        }

        @Test
        @DisplayName("Should validate API URL parameters")
        void shouldValidateAPIURLParameters() {
            assertThatThrownBy(() -> new HetznerCloudAPI("test-token", (String) null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("API URL cannot be null or blank");

            assertThatThrownBy(() -> new HetznerCloudAPI("test-token", ""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("API URL cannot be null or blank");
        }

        @Test
        @DisplayName("Should validate token parameters")
        void shouldValidateTokenParameters() {
            assertThatThrownBy(() -> new HetznerCloudAPI(null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("no Hetzner cloud token provided");

            assertThatThrownBy(() -> new HetznerCloudAPI(""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("no Hetzner cloud token provided");
        }
    }

    @Nested
    @DisplayName("Backward Compatibility")
    class BackwardCompatibilityTests {

        @Test
        @DisplayName("Should support both old and new constructor patterns")
        void shouldSupportBothOldAndNewConstructorPatterns() {
            // Old patterns
            HetznerCloudAPI oldStyle1 = new HetznerCloudAPI("token");
            HetznerCloudAPI oldStyle2 = new HetznerCloudAPI("token", new okhttp3.OkHttpClient());

            // New patterns
            HetznerCloudAPI newStyle1 = new HetznerCloudAPI("token", APIType.CLOUD);
            HetznerCloudAPI newStyle2 = new HetznerCloudAPI("token", "https://custom.api.com/v1");
            HetznerCloudAPI newStyle3 = new HetznerCloudAPI("token", APIType.HETZNER_ONLINE, new okhttp3.OkHttpClient());

            assertThat(oldStyle1).isNotNull();
            assertThat(oldStyle2).isNotNull();
            assertThat(newStyle1).isNotNull();
            assertThat(newStyle2).isNotNull();
            assertThat(newStyle3).isNotNull();
        }
    }
}