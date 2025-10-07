package io.github.sinuscosinustan.hetznercloud.unit.facility;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.response.DatacenterResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.DatacentersResponse;
import io.github.sinuscosinustan.hetznercloud.unit.OpenAPIMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

class DatacenterTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("Datacenter Retrieval")
    class DatacenterRetrievalTests {

        @Test
        @DisplayName("Should get single datacenter by ID")
        void shouldGetDatacenterById() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/datacenters/1", 200, """
                {
                  "datacenter": {
                    "id": 1,
                    "name": "fsn1-dc8",
                    "description": "Falkenstein 1 DC8",
                    "location": {
                      "id": 1,
                      "name": "fsn1",
                      "description": "Falkenstein DC Park 1",
                      "country": "DE",
                      "city": "Falkenstein",
                      "latitude": 50.47612,
                      "longitude": 12.370071,
                      "network_zone": "eu-central"
                    },
                    "server_types": {
                      "supported": [1, 2, 3],
                      "available": [1, 2, 3],
                      "available_for_migration": [1, 2, 3]
                    }
                  }
                }
                """);

            DatacenterResponse response = api.getDatacenter(1);

            assertThat(response).isNotNull();
            assertThat(response.getDatacenter()).isNotNull();
            assertThat(response.getDatacenter().getId()).isEqualTo(1L);
            assertThat(response.getDatacenter().getName()).isEqualTo("fsn1-dc8");
            assertThat(response.getDatacenter().getDescription()).isEqualTo("Falkenstein 1 DC8");
            assertThat(response.getDatacenter().getLocation()).isNotNull();
            assertThat(response.getDatacenter().getLocation().getName()).isEqualTo("fsn1");
            assertThat(response.getDatacenter().getServerTypes()).isNotNull();
        }

        @Test
        @DisplayName("Should get all datacenters")
        void shouldGetAllDatacenters() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/datacenters", 200, """
                {
                  "datacenters": [
                    {
                      "id": 1,
                      "name": "fsn1-dc8",
                      "description": "Falkenstein 1 DC8",
                      "location": {
                        "id": 1,
                        "name": "fsn1",
                        "description": "Falkenstein DC Park 1",
                        "country": "DE",
                        "city": "Falkenstein",
                        "latitude": 50.47612,
                        "longitude": 12.370071,
                        "network_zone": "eu-central"
                      },
                      "server_types": {
                        "supported": [1, 2, 3],
                        "available": [1, 2, 3],
                        "available_for_migration": [1, 2, 3]
                      }
                    },
                    {
                      "id": 2,
                      "name": "nbg1-dc3",
                      "description": "Nuremberg 1 DC3",
                      "location": {
                        "id": 2,
                        "name": "nbg1",
                        "description": "Nuremberg DC Park 1",
                        "country": "DE",
                        "city": "Nuremberg",
                        "latitude": 49.452102,
                        "longitude": 11.076665,
                        "network_zone": "eu-central"
                      },
                      "server_types": {
                        "supported": [1, 2, 3],
                        "available": [1, 2, 3],
                        "available_for_migration": [1, 2, 3]
                      }
                    }
                  ]
                }
                """);

            DatacentersResponse response = api.getDatacenters();

            assertThat(response).isNotNull();
            assertThat(response.getDatacenters()).isNotNull().hasSize(2);

            var datacenter1 = response.getDatacenters().get(0);
            assertThat(datacenter1.getId()).isEqualTo(1L);
            assertThat(datacenter1.getName()).isEqualTo("fsn1-dc8");

            var datacenter2 = response.getDatacenters().get(1);
            assertThat(datacenter2.getId()).isEqualTo(2L);
            assertThat(datacenter2.getName()).isEqualTo("nbg1-dc3");
        }

        @Test
        @DisplayName("Should get datacenter by name")
        void shouldGetDatacenterByName() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/datacenters?name=fsn1-dc8", 200, """
                {
                  "datacenters": [
                    {
                      "id": 1,
                      "name": "fsn1-dc8",
                      "description": "Falkenstein 1 DC8",
                      "location": {
                        "id": 1,
                        "name": "fsn1",
                        "description": "Falkenstein DC Park 1",
                        "country": "DE",
                        "city": "Falkenstein",
                        "latitude": 50.47612,
                        "longitude": 12.370071,
                        "network_zone": "eu-central"
                      },
                      "server_types": {
                        "supported": [1, 2, 3],
                        "available": [1, 2, 3],
                        "available_for_migration": [1, 2, 3]
                      }
                    }
                  ]
                }
                """);

            DatacentersResponse response = api.getDatacenter("fsn1-dc8");

            assertThat(response).isNotNull();
            assertThat(response.getDatacenters()).isNotNull().hasSize(1);
            assertThat(response.getDatacenters().get(0).getName()).isEqualTo("fsn1-dc8");
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle datacenter not found")
        void shouldHandleDatacenterNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/datacenters/999", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "Datacenter not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getDatacenter(999))
                    .isInstanceOf(Exception.class);
        }
    }
}