package io.github.sinuscosinustan.hetznercloud.unit.facility;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.response.LocationResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.LocationsResponse;
import io.github.sinuscosinustan.hetznercloud.unit.OpenAPIMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

class LocationTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("Location Retrieval")
    class LocationRetrievalTests {

        @Test
        @DisplayName("Should get single location by ID")
        void shouldGetLocationById() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/locations/1", 200, """
                {
                  "location": {
                    "id": 1,
                    "name": "fsn1",
                    "description": "Falkenstein DC Park 1",
                    "country": "DE",
                    "city": "Falkenstein",
                    "latitude": 50.47612,
                    "longitude": 12.370071,
                    "network_zone": "eu-central"
                  }
                }
                """);

            LocationResponse response = api.getLocation(1);

            assertThat(response).isNotNull();
            assertThat(response.getLocation()).isNotNull();
            assertThat(response.getLocation().getId()).isEqualTo(1L);
            assertThat(response.getLocation().getName()).isEqualTo("fsn1");
            assertThat(response.getLocation().getDescription()).isEqualTo("Falkenstein DC Park 1");
            assertThat(response.getLocation().getCountry()).isEqualTo("DE");
            assertThat(response.getLocation().getCity()).isEqualTo("Falkenstein");
            assertThat(response.getLocation().getLatitude()).isEqualTo(50.47612);
            assertThat(response.getLocation().getLongitude()).isEqualTo(12.370071);
            assertThat(response.getLocation().getNetworkZone()).isEqualTo("eu-central");
        }

        @Test
        @DisplayName("Should get all locations")
        void shouldGetAllLocations() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/locations", 200, """
                {
                  "locations": [
                    {
                      "id": 1,
                      "name": "fsn1",
                      "description": "Falkenstein DC Park 1",
                      "country": "DE",
                      "city": "Falkenstein",
                      "latitude": 50.47612,
                      "longitude": 12.370071,
                      "network_zone": "eu-central"
                    },
                    {
                      "id": 2,
                      "name": "nbg1",
                      "description": "Nuremberg DC Park 1",
                      "country": "DE",
                      "city": "Nuremberg",
                      "latitude": 49.452102,
                      "longitude": 11.076665,
                      "network_zone": "eu-central"
                    },
                    {
                      "id": 3,
                      "name": "hel1",
                      "description": "Helsinki DC Park 1",
                      "country": "FI",
                      "city": "Helsinki",
                      "latitude": 60.169857,
                      "longitude": 24.938379,
                      "network_zone": "eu-central"
                    }
                  ]
                }
                """);

            LocationsResponse response = api.getLocations();

            assertThat(response).isNotNull();
            assertThat(response.getLocations()).isNotNull().hasSize(3);

            var location1 = response.getLocations().get(0);
            assertThat(location1.getId()).isEqualTo(1L);
            assertThat(location1.getName()).isEqualTo("fsn1");
            assertThat(location1.getCountry()).isEqualTo("DE");

            var location2 = response.getLocations().get(1);
            assertThat(location2.getId()).isEqualTo(2L);
            assertThat(location2.getName()).isEqualTo("nbg1");

            var location3 = response.getLocations().get(2);
            assertThat(location3.getId()).isEqualTo(3L);
            assertThat(location3.getName()).isEqualTo("hel1");
            assertThat(location3.getCountry()).isEqualTo("FI");
        }

        @Test
        @DisplayName("Should get location by name")
        void shouldGetLocationByName() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/locations?name=fsn1", 200, """
                {
                  "locations": [
                    {
                      "id": 1,
                      "name": "fsn1",
                      "description": "Falkenstein DC Park 1",
                      "country": "DE",
                      "city": "Falkenstein",
                      "latitude": 50.47612,
                      "longitude": 12.370071,
                      "network_zone": "eu-central"
                    }
                  ]
                }
                """);

            LocationsResponse response = api.getLocationByName("fsn1");

            assertThat(response).isNotNull();
            assertThat(response.getLocations()).isNotNull().hasSize(1);
            assertThat(response.getLocations().get(0).getName()).isEqualTo("fsn1");
            assertThat(response.getLocations().get(0).getDescription()).isEqualTo("Falkenstein DC Park 1");
        }

        @Test
        @DisplayName("Should handle empty location search results")
        void shouldHandleEmptyLocationSearchResults() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/locations?name=nonexistent", 200, """
                {
                  "locations": []
                }
                """);

            LocationsResponse response = api.getLocationByName("nonexistent");

            assertThat(response).isNotNull();
            assertThat(response.getLocations()).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle location not found")
        void shouldHandleLocationNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/locations/999", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "Location not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getLocation(999))
                    .isInstanceOf(Exception.class);
        }
    }
}