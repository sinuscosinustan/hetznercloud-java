package io.github.sinuscosinustan.hetznercloud.unit.servers;

import io.github.sinuscosinustan.hetznercloud.objects.general.ServerType;
import io.github.sinuscosinustan.hetznercloud.objects.general.ServerTypeLocation;
import io.github.sinuscosinustan.hetznercloud.objects.general.Deprecation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class ServerTypeTest {

    @Nested
    @DisplayName("Server Type Location Support")
    class ServerTypeLocationSupportTests {

        @Test
        @DisplayName("Should support ServerTypeLocation with all fields")
        void shouldSupportServerTypeLocationWithAllFields() {
            ServerTypeLocation location = new ServerTypeLocation();
            location.setId(1L);
            location.setName("fsn1");

            Deprecation deprecation = new Deprecation();
            deprecation.setAnnounced(new Date());
            deprecation.setUnavailable_after(new Date());
            location.setDeprecation(deprecation);

            assertThat(location.getId()).isEqualTo(1L);
            assertThat(location.getName()).isEqualTo("fsn1");
            assertThat(location.getDeprecation()).isNotNull();
            assertThat(location.getDeprecation().getAnnounced()).isNotNull();
            assertThat(location.getDeprecation().getUnavailable_after()).isNotNull();
        }

        @Test
        @DisplayName("Should support updated ServerType with new fields")
        void shouldSupportUpdatedServerTypeWithNewFields() {
            ServerType serverType = new ServerType();
            serverType.setId(1L);
            serverType.setName("cx11");
            serverType.setCategory("shared vCPU");

            // Create location data
            ServerTypeLocation location1 = new ServerTypeLocation();
            location1.setId(1L);
            location1.setName("fsn1");
            location1.setDeprecation(null); // Not deprecated

            ServerTypeLocation location2 = new ServerTypeLocation();
            location2.setId(2L);
            location2.setName("nbg1");
            Deprecation deprecation = new Deprecation();
            deprecation.setAnnounced(new Date());
            location2.setDeprecation(deprecation); // Deprecated in this location

            serverType.setLocations(Arrays.asList(location1, location2));

            // Verify new structure
            assertThat(serverType.getCategory()).isEqualTo("shared vCPU");
            assertThat(serverType.getLocations()).hasSize(2);
            assertThat(serverType.getLocations().get(0).getDeprecation()).isNull();
            assertThat(serverType.getLocations().get(1).getDeprecation()).isNotNull();
        }

        @Test
        @DisplayName("Should serialize and deserialize ServerType with new structure")
        void shouldSerializeAndDeserializeServerTypeWithNewStructure() throws Exception {
            ObjectMapper mapper = new ObjectMapper();

            String json = """
                {
                  "id": 1,
                  "name": "cx11",
                  "category": "shared vCPU",
                  "locations": [
                    {
                      "id": 1,
                      "name": "fsn1",
                      "deprecation": null
                    },
                    {
                      "id": 2,
                      "name": "nbg1",
                      "deprecation": {
                        "announced": "2024-01-01T00:00:00Z",
                        "unavailable_after": "2024-12-31T23:59:59Z"
                      }
                    }
                  ]
                }
                """;

            ServerType serverType = mapper.readValue(json, ServerType.class);

            assertThat(serverType.getId()).isEqualTo(1L);
            assertThat(serverType.getName()).isEqualTo("cx11");
            assertThat(serverType.getCategory()).isEqualTo("shared vCPU");
            assertThat(serverType.getLocations()).hasSize(2);
            assertThat(serverType.getLocations().get(0).getDeprecation()).isNull();
            assertThat(serverType.getLocations().get(1).getDeprecation()).isNotNull();
        }

        @Test
        @DisplayName("Should maintain deprecated ServerType fields")
        void shouldMaintainDeprecatedServerTypeFields() {
            ServerType serverType = new ServerType();

            // Old deprecated fields should still be accessible
            serverType.setDeprecated(true);

            Deprecation oldDeprecation = new Deprecation();
            oldDeprecation.setAnnounced(new Date());
            serverType.setDeprecation(oldDeprecation);

            // These should work but generate deprecation warnings
            @SuppressWarnings("deprecation")
            Boolean deprecated = serverType.getDeprecated();
            @SuppressWarnings("deprecation")
            Deprecation deprecation = serverType.getDeprecation();

            assertThat(deprecated).isTrue();
            assertThat(deprecation).isNotNull();
        }
    }

    @Nested
    @DisplayName("JSON Serialization")
    class JSONSerializationTests {

        @Test
        @DisplayName("Should deserialize server type location data correctly")
        void shouldDeserializeServerTypeLocationDataCorrectly() throws Exception {
            ObjectMapper mapper = new ObjectMapper();

            String json = """
                {
                  "id": 1,
                  "name": "fsn1",
                  "deprecation": {
                    "announced": "2024-01-01T00:00:00Z",
                    "unavailable_after": "2024-12-31T23:59:59Z"
                  }
                }
                """;

            ServerTypeLocation location = mapper.readValue(json, ServerTypeLocation.class);

            assertThat(location.getId()).isEqualTo(1L);
            assertThat(location.getName()).isEqualTo("fsn1");
            assertThat(location.getDeprecation()).isNotNull();
            assertThat(location.getDeprecation().getAnnounced()).isNotNull();
            assertThat(location.getDeprecation().getUnavailable_after()).isNotNull();
        }
    }
}