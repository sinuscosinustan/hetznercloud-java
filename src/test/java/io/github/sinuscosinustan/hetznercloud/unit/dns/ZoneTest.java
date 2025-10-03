package io.github.sinuscosinustan.hetznercloud.unit.dns;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ZoneMode;
import io.github.sinuscosinustan.hetznercloud.objects.general.Zone;
import io.github.sinuscosinustan.hetznercloud.objects.request.CreateZoneRequest;
import io.github.sinuscosinustan.hetznercloud.objects.request.UpdateZoneRequest;
import io.github.sinuscosinustan.hetznercloud.objects.response.ActionResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.ZoneResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.ZonesResponse;
import io.github.sinuscosinustan.hetznercloud.unit.OpenAPIMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ZoneTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("Zone Retrieval")
    class ZoneRetrievalTests {

        @Test
        @DisplayName("Should get all DNS zones")
        void shouldGetAllZones() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones", 200, """
                {
                  "zones": [
                    {
                      "id": 42,
                      "name": "example.com",
                      "created": "2016-01-30T23:55:00+00:00",
                      "mode": "primary",
                      "primary_nameservers": [],
                      "labels": {
                        "environment": "prod"
                      },
                      "protection": {
                        "delete": false
                      },
                      "ttl": 10800,
                      "status": "ok",
                      "record_count": 5,
                      "authoritative_nameservers": {
                        "assigned": [
                          "hydrogen.ns.hetzner.com.",
                          "oxygen.ns.hetzner.com.",
                          "helium.ns.hetzner.de."
                        ],
                        "delegated": [
                          "hydrogen.ns.hetzner.com.",
                          "oxygen.ns.hetzner.com.",
                          "helium.ns.hetzner.de."
                        ],
                        "delegation_last_check": "2016-01-30T23:55:00+00:00",
                        "delegation_status": "valid"
                      },
                      "registrar": "hetzner"
                    }
                  ],
                  "meta": {
                    "pagination": {
                      "page": 1,
                      "per_page": 25,
                      "previous_page": null,
                      "next_page": null,
                      "last_page": 1,
                      "total_entries": 1
                    }
                  }
                }
                """);

            ZonesResponse response = api.getZones();

            assertThat(response).isNotNull();
            assertThat(response.getZones()).isNotNull().hasSize(1);

            Zone zone = response.getZones().get(0);
            assertThat(zone.getId()).isEqualTo(42L);
            assertThat(zone.getName()).isEqualTo("example.com");
            assertThat(zone.getMode()).isEqualTo(ZoneMode.PRIMARY);
            assertThat(zone.getTtl()).isEqualTo(10800L);
            assertThat(zone.getStatus()).isEqualTo("ok");
            assertThat(zone.getRecordCount()).isEqualTo(5);
            assertThat(zone.getRegistrar()).isEqualTo("hetzner");
            assertThat(zone.getLabels()).containsEntry("environment", "prod");
            assertThat(zone.getProtection().getDelete()).isFalse();
            assertThat(zone.getAuthoritativeNameservers().getAssigned())
                    .containsExactly("hydrogen.ns.hetzner.com.", "oxygen.ns.hetzner.com.", "helium.ns.hetzner.de.");
            assertThat(zone.getAuthoritativeNameservers().getDelegationStatus()).isEqualTo("valid");
        }

        @Test
        @DisplayName("Should get single DNS zone by ID")
        void shouldGetZoneById() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42", 200, """
                {
                  "zone": {
                    "id": 42,
                    "name": "example.com",
                    "created": "2016-01-30T23:55:00+00:00",
                    "mode": "primary",
                    "primary_nameservers": [],
                    "labels": {},
                    "protection": {
                      "delete": false
                    },
                    "ttl": 3600,
                    "status": "ok",
                    "record_count": 3,
                    "authoritative_nameservers": {
                      "assigned": [
                        "hydrogen.ns.hetzner.com.",
                        "oxygen.ns.hetzner.com."
                      ],
                      "delegated": [
                        "hydrogen.ns.hetzner.com.",
                        "oxygen.ns.hetzner.com."
                      ],
                      "delegation_last_check": "2016-01-30T23:55:00+00:00",
                      "delegation_status": "valid"
                    },
                    "registrar": "other"
                  }
                }
                """);

            ZoneResponse response = api.getZone(42);

            assertThat(response).isNotNull();
            assertThat(response.getZone()).isNotNull();
            assertThat(response.getZone().getId()).isEqualTo(42L);
            assertThat(response.getZone().getName()).isEqualTo("example.com");
            assertThat(response.getZone().getTtl()).isEqualTo(3600L);
            assertThat(response.getZone().getRecordCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should get DNS zone by name")
        void shouldGetZoneByName() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones?name=example.com", 200, """
                {
                  "zones": [
                    {
                      "id": 42,
                      "name": "example.com",
                      "created": "2016-01-30T23:55:00+00:00",
                      "mode": "primary",
                      "primary_nameservers": [],
                      "labels": {},
                      "protection": {
                        "delete": false
                      },
                      "ttl": 3600,
                      "status": "ok",
                      "record_count": 1,
                      "authoritative_nameservers": {
                        "assigned": ["hydrogen.ns.hetzner.com."],
                        "delegated": ["hydrogen.ns.hetzner.com."],
                        "delegation_last_check": "2016-01-30T23:55:00+00:00",
                        "delegation_status": "valid"
                      },
                      "registrar": "unknown"
                    }
                  ]
                }
                """);

            ZonesResponse response = api.getZoneByName("example.com");

            assertThat(response).isNotNull();
            assertThat(response.getZones()).isNotNull().hasSize(1);
            assertThat(response.getZones().get(0).getName()).isEqualTo("example.com");
        }

        @Test
        @DisplayName("Should get secondary DNS zone with primary nameservers")
        void shouldGetSecondaryZone() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/43", 200, """
                {
                  "zone": {
                    "id": 43,
                    "name": "secondary.example.com",
                    "created": "2016-01-30T23:55:00+00:00",
                    "mode": "secondary",
                    "primary_nameservers": [
                      {
                        "address": "198.51.100.1",
                        "port": 53
                      },
                      {
                        "address": "203.0.113.1",
                        "port": 5353,
                        "tsig_key": "secret-key",
                        "tsig_algorithm": "hmac-sha256"
                      }
                    ],
                    "labels": {
                      "type": "secondary"
                    },
                    "protection": {
                      "delete": true
                    },
                    "ttl": 7200,
                    "status": "updating",
                    "record_count": 10,
                    "authoritative_nameservers": {
                      "assigned": ["hydrogen.ns.hetzner.com."],
                      "delegated": ["hydrogen.ns.hetzner.com."],
                      "delegation_last_check": null,
                      "delegation_status": "unknown"
                    },
                    "registrar": "other"
                  }
                }
                """);

            ZoneResponse response = api.getZone(43);

            assertThat(response).isNotNull();
            Zone zone = response.getZone();
            assertThat(zone.getMode()).isEqualTo(ZoneMode.SECONDARY);
            assertThat(zone.getPrimaryNameservers()).hasSize(2);

            Zone.PrimaryNameserver ns1 = zone.getPrimaryNameservers().get(0);
            assertThat(ns1.getAddress()).isEqualTo("198.51.100.1");
            assertThat(ns1.getPort()).isEqualTo(53);
            assertThat(ns1.getTsigKey()).isNull();

            Zone.PrimaryNameserver ns2 = zone.getPrimaryNameservers().get(1);
            assertThat(ns2.getAddress()).isEqualTo("203.0.113.1");
            assertThat(ns2.getPort()).isEqualTo(5353);
            assertThat(ns2.getTsigKey()).isEqualTo("secret-key");
            assertThat(ns2.getTsigAlgorithm()).isEqualTo("hmac-sha256");

            assertThat(zone.getProtection().getDelete()).isTrue();
            assertThat(zone.getStatus()).isEqualTo("updating");
        }
    }

    @Nested
    @DisplayName("Zone Management")
    class ZoneManagementTests {

        @Test
        @DisplayName("Should create primary DNS zone")
        void shouldCreatePrimaryZone() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones", 201, """
                {
                  "zone": {
                    "id": 100,
                    "name": "newzone.com",
                    "created": "2016-01-30T23:55:00+00:00",
                    "mode": "primary",
                    "primary_nameservers": [],
                    "labels": {
                      "project": "test"
                    },
                    "protection": {
                      "delete": false
                    },
                    "ttl": 7200,
                    "status": "ok",
                    "record_count": 2,
                    "authoritative_nameservers": {
                      "assigned": ["hydrogen.ns.hetzner.com."],
                      "delegated": ["hydrogen.ns.hetzner.com."],
                      "delegation_last_check": "2016-01-30T23:55:00+00:00",
                      "delegation_status": "valid"
                    },
                    "registrar": "hetzner"
                  },
                  "actions": []
                }
                """);

            CreateZoneRequest request = new CreateZoneRequest(
                    "newzone.com",
                    7200L,
                    ZoneMode.PRIMARY,
                    null,
                    Map.of("project", "test")
            );

            ZoneResponse response = api.createZone(request);

            assertThat(response).isNotNull();
            assertThat(response.getZone()).isNotNull();
            assertThat(response.getZone().getId()).isEqualTo(100L);
            assertThat(response.getZone().getName()).isEqualTo("newzone.com");
            assertThat(response.getZone().getMode()).isEqualTo(ZoneMode.PRIMARY);
            assertThat(response.getZone().getTtl()).isEqualTo(7200L);
            assertThat(response.getZone().getLabels()).containsEntry("project", "test");
        }

        @Test
        @DisplayName("Should create secondary DNS zone")
        void shouldCreateSecondaryZone() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones", 201, """
                {
                  "zone": {
                    "id": 101,
                    "name": "secondary.com",
                    "created": "2016-01-30T23:55:00+00:00",
                    "mode": "secondary",
                    "primary_nameservers": [
                      {
                        "address": "192.168.1.1",
                        "port": 53
                      }
                    ],
                    "labels": {},
                    "protection": {
                      "delete": false
                    },
                    "ttl": 3600,
                    "status": "updating",
                    "record_count": 0,
                    "authoritative_nameservers": {
                      "assigned": ["hydrogen.ns.hetzner.com."],
                      "delegated": [],
                      "delegation_last_check": null,
                      "delegation_status": "unknown"
                    },
                    "registrar": "other"
                  },
                  "actions": []
                }
                """);

            Zone.PrimaryNameserver primaryNs = new Zone.PrimaryNameserver();
            primaryNs.setAddress("192.168.1.1");
            primaryNs.setPort(53);

            CreateZoneRequest request = new CreateZoneRequest(
                    "secondary.com",
                    3600L,
                    ZoneMode.SECONDARY,
                    List.of(primaryNs),
                    Map.of()
            );

            ZoneResponse response = api.createZone(request);

            assertThat(response).isNotNull();
            assertThat(response.getZone().getMode()).isEqualTo(ZoneMode.SECONDARY);
            assertThat(response.getZone().getPrimaryNameservers()).hasSize(1);
            assertThat(response.getZone().getPrimaryNameservers().get(0).getAddress()).isEqualTo("192.168.1.1");
        }

        @Test
        @DisplayName("Should update DNS zone")
        void shouldUpdateZone() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("PUT", "/v1/zones/42", 200, """
                {
                  "zone": {
                    "id": 42,
                    "name": "updated.example.com",
                    "created": "2016-01-30T23:55:00+00:00",
                    "mode": "primary",
                    "primary_nameservers": [],
                    "labels": {
                      "updated": "true"
                    },
                    "protection": {
                      "delete": false
                    },
                    "ttl": 1800,
                    "status": "ok",
                    "record_count": 3,
                    "authoritative_nameservers": {
                      "assigned": ["hydrogen.ns.hetzner.com."],
                      "delegated": ["hydrogen.ns.hetzner.com."],
                      "delegation_last_check": "2016-01-30T23:55:00+00:00",
                      "delegation_status": "valid"
                    },
                    "registrar": "hetzner"
                  }
                }
                """);

            UpdateZoneRequest request = new UpdateZoneRequest(
                    "updated.example.com",
                    1800L,
                    null,
                    Map.of("updated", "true")
            );

            ZoneResponse response = api.updateZone(42, request);

            assertThat(response).isNotNull();
            assertThat(response.getZone().getName()).isEqualTo("updated.example.com");
            assertThat(response.getZone().getTtl()).isEqualTo(1800L);
            assertThat(response.getZone().getLabels()).containsEntry("updated", "true");
        }

        @Test
        @DisplayName("Should delete DNS zone")
        void shouldDeleteZone() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("DELETE", "/v1/zones/42", 200, """
                {
                  "action": {
                    "id": 123,
                    "command": "delete_zone",
                    "status": "success",
                    "progress": 100,
                    "started": "2016-01-30T23:55:00+00:00",
                    "finished": "2016-01-30T23:56:00+00:00",
                    "resources": [
                      {
                        "id": 42,
                        "type": "zone"
                      }
                    ],
                    "error": null
                  }
                }
                """);

            ActionResponse response = api.deleteZone(42);

            assertThat(response).isNotNull();
            assertThat(response.getAction()).isNotNull();
            assertThat(response.getAction().getCommand()).isEqualTo("delete_zone");
            assertThat(response.getAction().getStatus()).isEqualTo("success");
        }
    }

    @Nested
    @DisplayName("Zone Actions")
    class ZoneActionsTests {

        @Test
        @DisplayName("Should change zone protection")
        void shouldChangeZoneProtection() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones/42/actions/change_protection", 201, """
                {
                  "action": {
                    "id": 456,
                    "command": "change_zone_protection",
                    "status": "running",
                    "progress": 0,
                    "started": "2016-01-30T23:55:00+00:00",
                    "finished": null,
                    "resources": [
                      {
                        "id": 42,
                        "type": "zone"
                      }
                    ],
                    "error": null
                  }
                }
                """);

            ActionResponse response = api.changeZoneProtection(42, true);

            assertThat(response).isNotNull();
            assertThat(response.getAction()).isNotNull();
            assertThat(response.getAction().getCommand()).isEqualTo("change_zone_protection");
            assertThat(response.getAction().getStatus()).isEqualTo("running");
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle zone not found")
        void shouldHandleZoneNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/999", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "Zone not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getZone(999))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should handle invalid zone creation")
        void shouldHandleInvalidZoneCreation() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones", 422, """
                {
                  "error": {
                    "code": "invalid_input",
                    "message": "invalid name",
                    "details": {
                      "fields": [
                        {
                          "name": "name",
                          "messages": ["is not a valid domain name"]
                        }
                      ]
                    }
                  }
                }
                """);

            CreateZoneRequest request = new CreateZoneRequest(
                    "invalid..domain",
                    3600L,
                    ZoneMode.PRIMARY,
                    null,
                    Map.of()
            );

            assertThatThrownBy(() -> api.createZone(request))
                    .isInstanceOf(Exception.class);
        }
    }
}