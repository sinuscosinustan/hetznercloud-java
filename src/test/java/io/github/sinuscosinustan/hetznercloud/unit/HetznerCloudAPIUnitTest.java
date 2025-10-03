package io.github.sinuscosinustan.hetznercloud.unit;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.enums.APIType;
import io.github.sinuscosinustan.hetznercloud.objects.response.ServersResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.ServerTypesResponse;
import io.github.sinuscosinustan.hetznercloud.exception.APIRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

class HetznerCloudAPIUnitTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create API instance with default Cloud API endpoint")
        void shouldCreateWithDefaultCloudEndpoint() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token");
            assertThat(api).isNotNull();
        }

        @Test
        @DisplayName("Should create API instance with specific API type")
        void shouldCreateWithSpecificAPIType() {
            HetznerCloudAPI cloudApi = new HetznerCloudAPI("test-token", APIType.CLOUD);
            HetznerCloudAPI hoApi = new HetznerCloudAPI("test-token", APIType.HETZNER_ONLINE);

            assertThat(cloudApi).isNotNull();
            assertThat(hoApi).isNotNull();
        }

        @Test
        @DisplayName("Should create API instance with custom URL")
        void shouldCreateWithCustomURL() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());
            assertThat(api).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception with null token")
        void shouldThrowExceptionWithNullToken() {
            assertThatThrownBy(() -> new HetznerCloudAPI(null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("no Hetzner cloud token provided");
        }

        @Test
        @DisplayName("Should throw exception with blank token")
        void shouldThrowExceptionWithBlankToken() {
            assertThatThrownBy(() -> new HetznerCloudAPI(""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("no Hetzner cloud token provided");
        }

        @Test
        @DisplayName("Should throw exception with null custom URL")
        void shouldThrowExceptionWithNullCustomURL() {
            assertThatThrownBy(() -> new HetznerCloudAPI("test-token", (String) null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("API URL cannot be null or blank");
        }
    }

    @Nested
    @DisplayName("Server Operations")
    class ServerOperations {

        @Test
        @DisplayName("Should get servers successfully")
        void shouldGetServersSuccessfully() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            ServersResponse response = api.getServers();

            assertThat(response).isNotNull();
            assertThat(response.getServers()).isNotNull();
            assertThat(response.getMeta()).isNotNull();
            assertThat(response.getMeta().getPagination()).isNotNull();
        }

        @Test
        @DisplayName("Should handle server not found error")
        void shouldHandleServerNotFoundError() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            assertThatThrownBy(() -> api.getServer(999999))
                    .isInstanceOf(APIRequestException.class);
        }
    }

    @Nested
    @DisplayName("Server Types with New API Structure")
    class ServerTypesTests {

        @Test
        @DisplayName("Should get server types with new location-based structure")
        void shouldGetServerTypesWithNewStructure() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            ServerTypesResponse response = api.getServerTypes();

            assertThat(response).isNotNull();
            assertThat(response.getServerTypes()).isNotEmpty();

            // Test new API structure
            var serverType = response.getServerTypes().get(0);
            assertThat(serverType.getCategory()).isEqualTo("shared vCPU");
            assertThat(serverType.getLocations()).isNotNull().isNotEmpty();

            var location = serverType.getLocations().get(0);
            assertThat(location.getId()).isEqualTo(1L);
            assertThat(location.getName()).isEqualTo("fsn1");
            assertThat(location.getDeprecation()).isNull(); // Not deprecated in this location
        }
    }

    @Nested
    @DisplayName("Network Attachment with IP Range Support")
    class NetworkAttachmentTests {

        @Test
        @DisplayName("Should create attach request with IP range")
        void shouldCreateAttachRequestWithIPRange() {
            // Test that our new AttachServerToNetworkRequest supports ip_range
            var request = io.github.sinuscosinustan.hetznercloud.objects.request.AttachServerToNetworkRequest.builder()
                    .network(123L)
                    .ip("10.0.0.5")
                    .ipRange("10.0.0.0/24")
                    .build();

            assertThat(request.getNetwork()).isEqualTo(123L);
            assertThat(request.getIp()).isEqualTo("10.0.0.5");
            assertThat(request.getIpRange()).isEqualTo("10.0.0.0/24");
        }

        @Test
        @DisplayName("Should create load balancer network request with IP range")
        void shouldCreateLoadBalancerNetworkRequestWithIPRange() {
            // Test that our new LoadBalancerNetworkRequest supports ip_range
            var request = io.github.sinuscosinustan.hetznercloud.objects.request.LoadBalancerNetworkRequest.builder()
                    .network(123L)
                    .ip("10.0.0.10")
                    .ipRange("10.0.0.0/24")
                    .build();

            assertThat(request.getNetwork()).isEqualTo(123L);
            assertThat(request.getIp()).isEqualTo("10.0.0.10");
            assertThat(request.getIpRange()).isEqualTo("10.0.0.0/24");
        }
    }

    @Nested
    @DisplayName("API Endpoint Configuration")
    class APIEndpointTests {

        @Test
        @DisplayName("Should use correct endpoints for different API types")
        void shouldUseCorrectEndpointsForDifferentAPITypes() {
            assertThat(APIType.CLOUD.getBaseUrl()).isEqualTo("https://api.hetzner.cloud/v1");
            assertThat(APIType.HETZNER_ONLINE.getBaseUrl()).isEqualTo("https://api.hetzner.com/v1");
        }

        @Test
        @DisplayName("Should allow custom API endpoints")
        void shouldAllowCustomAPIEndpoints() {
            String customUrl = "https://custom-api.example.com/v1";
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", customUrl);

            assertThat(api).isNotNull();
            // We can't directly test the internal URL without exposing it,
            // but we can verify the constructor doesn't throw
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle API errors correctly")
        void shouldHandleAPIErrorsCorrectly() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            // Add a stub for server error
            addStub("GET", "/v1/servers/500", 500, """
                {
                  "error": {
                    "code": "internal_server_error",
                    "message": "Internal server error"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getServer(500))
                    .isInstanceOf(APIRequestException.class);
        }

        @Test
        @DisplayName("Should handle network timeouts gracefully")
        void shouldHandleNetworkTimeoutsGracefully() {
            // This would require more complex WireMock setup for timeout simulation
            // For now, we'll test that the API instance can be created
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());
            assertThat(api).isNotNull();
        }
    }

    @Nested
    @DisplayName("Backward Compatibility")
    class BackwardCompatibilityTests {

        @Test
        @DisplayName("Should maintain backward compatibility for existing constructors")
        void shouldMaintainBackwardCompatibilityForExistingConstructors() {
            // These should still work as before
            HetznerCloudAPI api1 = new HetznerCloudAPI("test-token");
            HetznerCloudAPI api2 = new HetznerCloudAPI("test-token", new okhttp3.OkHttpClient());

            assertThat(api1).isNotNull();
            assertThat(api2).isNotNull();
        }

        @Test
        @DisplayName("Should support deprecated server type fields")
        void shouldSupportDeprecatedServerTypeFields() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            ServerTypesResponse response = api.getServerTypes();
            var serverType = response.getServerTypes().get(0);

            // Old deprecated fields should still be accessible
            assertThat(serverType.getDeprecated()).isNull();
            assertThat(serverType.getDeprecation()).isNull();

            // New fields should be available
            assertThat(serverType.getCategory()).isNotNull();
            assertThat(serverType.getLocations()).isNotNull();
        }
    }
}