package io.github.sinuscosinustan.hetznercloud.unit.network;

import io.github.sinuscosinustan.hetznercloud.objects.request.AttachServerToNetworkRequest;
import io.github.sinuscosinustan.hetznercloud.objects.request.LoadBalancerNetworkRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class IPRangeSupportTest {

    @Nested
    @DisplayName("Server Network Attachment")
    class ServerNetworkAttachmentTests {

        @Test
        @DisplayName("Should support IP range in AttachServerToNetworkRequest")
        void shouldSupportIPRangeInAttachServerToNetworkRequest() {
            // Test builder pattern with new ip_range field
            AttachServerToNetworkRequest request = AttachServerToNetworkRequest.builder()
                    .network(123L)
                    .ip("10.0.0.5")
                    .ipRange("10.0.0.0/24")
                    .aliasIps(Arrays.asList("10.0.0.10", "10.0.0.11"))
                    .build();

            assertThat(request.getNetwork()).isEqualTo(123L);
            assertThat(request.getIp()).isEqualTo("10.0.0.5");
            assertThat(request.getIpRange()).isEqualTo("10.0.0.0/24");
            assertThat(request.getAliasIps()).containsExactly("10.0.0.10", "10.0.0.11");
        }

        @Test
        @DisplayName("Should support IP range only without specific IP")
        void shouldSupportIPRangeOnlyWithoutSpecificIP() {
            AttachServerToNetworkRequest request = AttachServerToNetworkRequest.builder()
                    .network(456L)
                    .ipRange("10.1.0.0/24") // Only IP range, no specific IP
                    .build();

            assertThat(request.getNetwork()).isEqualTo(456L);
            assertThat(request.getIp()).isNull();
            assertThat(request.getIpRange()).isEqualTo("10.1.0.0/24");
        }

        @Test
        @DisplayName("Should serialize IP range fields correctly")
        void shouldSerializeIPRangeFieldsCorrectly() throws Exception {
            ObjectMapper mapper = new ObjectMapper();

            AttachServerToNetworkRequest request = AttachServerToNetworkRequest.builder()
                    .network(123L)
                    .ipRange("10.0.0.0/24")
                    .build();

            String json = mapper.writeValueAsString(request);

            assertThat(json).contains("\"network\":123");
            assertThat(json).contains("\"ip_range\":\"10.0.0.0/24\"");
            assertThat(json).doesNotContain("\"ip\":"); // Should not include null fields
        }
    }

    @Nested
    @DisplayName("Load Balancer Network Attachment")
    class LoadBalancerNetworkAttachmentTests {

        @Test
        @DisplayName("Should support IP range in LoadBalancerNetworkRequest")
        void shouldSupportIPRangeInLoadBalancerNetworkRequest() {
            LoadBalancerNetworkRequest request = LoadBalancerNetworkRequest.builder()
                    .network(789L)
                    .ip("10.2.0.5")
                    .ipRange("10.2.0.0/24")
                    .build();

            assertThat(request.getNetwork()).isEqualTo(789L);
            assertThat(request.getIp()).isEqualTo("10.2.0.5");
            assertThat(request.getIpRange()).isEqualTo("10.2.0.0/24");
        }

        @Test
        @DisplayName("Should maintain backward compatibility for network requests")
        void shouldMaintainBackwardCompatibilityForNetworkRequests() {
            // Old style constructors should still work
            LoadBalancerNetworkRequest oldStyleRequest = new LoadBalancerNetworkRequest(123L);
            LoadBalancerNetworkRequest oldStyleWithIP = new LoadBalancerNetworkRequest(456L, "10.0.0.1");

            assertThat(oldStyleRequest.getNetwork()).isEqualTo(123L);
            assertThat(oldStyleRequest.getIp()).isNull();
            assertThat(oldStyleRequest.getIpRange()).isNull();

            assertThat(oldStyleWithIP.getNetwork()).isEqualTo(456L);
            assertThat(oldStyleWithIP.getIp()).isEqualTo("10.0.0.1");
            assertThat(oldStyleWithIP.getIpRange()).isNull();
        }
    }

    @Nested
    @DisplayName("JSON Serialization")
    class JSONSerializationTests {

        @Test
        @DisplayName("Should handle null values in network requests")
        void shouldHandleNullValuesInNetworkRequests() throws Exception {
            ObjectMapper mapper = new ObjectMapper();

            AttachServerToNetworkRequest request = AttachServerToNetworkRequest.builder()
                    .network(123L)
                    .build();

            String json = mapper.writeValueAsString(request);

            // Should only include non-null fields due to @JsonInclude(JsonInclude.Include.NON_NULL)
            assertThat(json).contains("\"network\":123");
            assertThat(json).doesNotContain("\"ip\":");
            assertThat(json).doesNotContain("\"ip_range\":");
            assertThat(json).doesNotContain("\"alias_ips\":");
        }
    }
}