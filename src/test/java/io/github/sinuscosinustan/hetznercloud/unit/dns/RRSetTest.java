package io.github.sinuscosinustan.hetznercloud.unit.dns;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.enums.RRSetType;
import io.github.sinuscosinustan.hetznercloud.objects.general.RRSet;
import io.github.sinuscosinustan.hetznercloud.objects.general.Record;
import io.github.sinuscosinustan.hetznercloud.objects.request.CreateRRSetRequest;
import io.github.sinuscosinustan.hetznercloud.objects.request.UpdateRRSetRequest;
import io.github.sinuscosinustan.hetznercloud.objects.response.ActionResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.RRSetResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.RRSetsResponse;
import io.github.sinuscosinustan.hetznercloud.unit.OpenAPIMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class RRSetTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("RRSet Retrieval")
    class RRSetRetrievalTests {

        @Test
        @DisplayName("Should get all RRSets for a zone")
        void shouldGetAllRRSets() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets", 200, """
                {
                  "rrsets": [
                    {
                      "id": "www/A",
                      "name": "www",
                      "type": "A",
                      "ttl": 3600,
                      "labels": {
                        "environment": "prod"
                      },
                      "protection": {
                        "change": false
                      },
                      "records": [
                        {
                          "value": "198.51.100.1",
                          "comment": "My web server at Hetzner Cloud."
                        }
                      ],
                      "zone": 42
                    },
                    {
                      "id": "mail/MX",
                      "name": "mail",
                      "type": "MX",
                      "ttl": 7200,
                      "labels": {},
                      "protection": {
                        "change": true
                      },
                      "records": [
                        {
                          "value": "10 mail.example.com.",
                          "comment": "Primary mail server"
                        },
                        {
                          "value": "20 backup.example.com.",
                          "comment": "Backup mail server"
                        }
                      ],
                      "zone": 42
                    }
                  ],
                  "meta": {
                    "pagination": {
                      "page": 1,
                      "per_page": 25,
                      "previous_page": null,
                      "next_page": null,
                      "last_page": 1,
                      "total_entries": 2
                    }
                  }
                }
                """);

            RRSetsResponse response = api.getRRSets(42);

            assertThat(response).isNotNull();
            assertThat(response.getRrsets()).isNotNull().hasSize(2);

            RRSet aRecord = response.getRrsets().get(0);
            assertThat(aRecord.getId()).isEqualTo("www/A");
            assertThat(aRecord.getName()).isEqualTo("www");
            assertThat(aRecord.getType()).isEqualTo(RRSetType.A);
            assertThat(aRecord.getTtl()).isEqualTo(3600L);
            assertThat(aRecord.getZone()).isEqualTo(42L);
            assertThat(aRecord.getProtection().getChange()).isFalse();
            assertThat(aRecord.getRecords()).hasSize(1);
            assertThat(aRecord.getRecords().get(0).getValue()).isEqualTo("198.51.100.1");
            assertThat(aRecord.getRecords().get(0).getComment()).isEqualTo("My web server at Hetzner Cloud.");

            RRSet mxRecord = response.getRrsets().get(1);
            assertThat(mxRecord.getType()).isEqualTo(RRSetType.MX);
            assertThat(mxRecord.getProtection().getChange()).isTrue();
            assertThat(mxRecord.getRecords()).hasSize(2);
        }

        @Test
        @DisplayName("Should get single RRSet by ID")
        void shouldGetRRSetById() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets/www/A", 200, """
                {
                  "rrset": {
                    "id": "www/A",
                    "name": "www",
                    "type": "A",
                    "ttl": 1800,
                    "labels": {
                      "managed": "true"
                    },
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "203.0.113.1",
                        "comment": "Updated server IP"
                      }
                    ],
                    "zone": 42
                  }
                }
                """);

            RRSetResponse response = api.getRRSet(42, "www/A");

            assertThat(response).isNotNull();
            assertThat(response.getRrset()).isNotNull();
            assertThat(response.getRrset().getId()).isEqualTo("www/A");
            assertThat(response.getRrset().getName()).isEqualTo("www");
            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.A);
            assertThat(response.getRrset().getTtl()).isEqualTo(1800L);
            assertThat(response.getRrset().getRecords().get(0).getValue()).isEqualTo("203.0.113.1");
        }

        @Test
        @DisplayName("Should get AAAA RRSet")
        void shouldGetAAAARRSet() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets/www/AAAA", 200, """
                {
                  "rrset": {
                    "id": "www/AAAA",
                    "name": "www",
                    "type": "AAAA",
                    "ttl": 3600,
                    "labels": {},
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "2001:db8::1",
                        "comment": "IPv6 address"
                      }
                    ],
                    "zone": 42
                  }
                }
                """);

            RRSetResponse response = api.getRRSet(42, "www/AAAA");

            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.AAAA);
            assertThat(response.getRrset().getRecords().get(0).getValue()).isEqualTo("2001:db8::1");
        }

        @Test
        @DisplayName("Should get TXT RRSet with multiple records")
        void shouldGetTXTRRSet() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets/@/TXT", 200, """
                {
                  "rrset": {
                    "id": "@/TXT",
                    "name": "@",
                    "type": "TXT",
                    "ttl": 300,
                    "labels": {
                      "verification": "true"
                    },
                    "protection": {
                      "change": true
                    },
                    "records": [
                      {
                        "value": "v=spf1 include:_spf.hetzner.com ~all",
                        "comment": "SPF record"
                      },
                      {
                        "value": "google-site-verification=abc123",
                        "comment": "Google verification"
                      }
                    ],
                    "zone": 42
                  }
                }
                """);

            RRSetResponse response = api.getRRSet(42, "@/TXT");

            assertThat(response.getRrset().getName()).isEqualTo("@");
            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.TXT);
            assertThat(response.getRrset().getRecords()).hasSize(2);
            assertThat(response.getRrset().getRecords().get(0).getValue()).contains("spf1");
            assertThat(response.getRrset().getRecords().get(1).getValue()).contains("google-site-verification");
        }
    }

    @Nested
    @DisplayName("RRSet Management")
    class RRSetManagementTests {

        @Test
        @DisplayName("Should create A record RRSet")
        void shouldCreateARecord() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones/42/rrsets", 201, """
                {
                  "rrset": {
                    "id": "api/A",
                    "name": "api",
                    "type": "A",
                    "ttl": 3600,
                    "labels": {
                      "service": "api"
                    },
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "192.168.1.100",
                        "comment": "API server"
                      }
                    ],
                    "zone": 42
                  },
                  "actions": []
                }
                """);

            Record record = new Record();
            record.setValue("192.168.1.100");
            record.setComment("API server");

            CreateRRSetRequest request = new CreateRRSetRequest(
                    "api",
                    RRSetType.A,
                    3600L,
                    Map.of("service", "api"),
                    List.of(record)
            );

            RRSetResponse response = api.createRRSet(42, request);

            assertThat(response).isNotNull();
            assertThat(response.getRrset()).isNotNull();
            assertThat(response.getRrset().getId()).isEqualTo("api/A");
            assertThat(response.getRrset().getName()).isEqualTo("api");
            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.A);
            assertThat(response.getRrset().getRecords()).hasSize(1);
            assertThat(response.getRrset().getRecords().get(0).getValue()).isEqualTo("192.168.1.100");
        }

        @Test
        @DisplayName("Should create MX record RRSet")
        void shouldCreateMXRecord() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones/42/rrsets", 201, """
                {
                  "rrset": {
                    "id": "@/MX",
                    "name": "@",
                    "type": "MX",
                    "ttl": 86400,
                    "labels": {},
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "10 mx1.example.com.",
                        "comment": "Primary MX"
                      },
                      {
                        "value": "20 mx2.example.com.",
                        "comment": "Secondary MX"
                      }
                    ],
                    "zone": 42
                  },
                  "actions": []
                }
                """);

            Record primary = new Record();
            primary.setValue("10 mx1.example.com.");
            primary.setComment("Primary MX");

            Record secondary = new Record();
            secondary.setValue("20 mx2.example.com.");
            secondary.setComment("Secondary MX");

            CreateRRSetRequest request = new CreateRRSetRequest(
                    "@",
                    RRSetType.MX,
                    86400L,
                    Map.of(),
                    List.of(primary, secondary)
            );

            RRSetResponse response = api.createRRSet(42, request);

            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.MX);
            assertThat(response.getRrset().getRecords()).hasSize(2);
        }

        @Test
        @DisplayName("Should create CNAME record RRSet")
        void shouldCreateCNAMERecord() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones/42/rrsets", 201, """
                {
                  "rrset": {
                    "id": "blog/CNAME",
                    "name": "blog",
                    "type": "CNAME",
                    "ttl": 1800,
                    "labels": {
                      "alias": "true"
                    },
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "www.example.com.",
                        "comment": "Blog alias"
                      }
                    ],
                    "zone": 42
                  },
                  "actions": []
                }
                """);

            Record record = new Record();
            record.setValue("www.example.com.");
            record.setComment("Blog alias");

            CreateRRSetRequest request = new CreateRRSetRequest(
                    "blog",
                    RRSetType.CNAME,
                    1800L,
                    Map.of("alias", "true"),
                    List.of(record)
            );

            RRSetResponse response = api.createRRSet(42, request);

            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.CNAME);
            assertThat(response.getRrset().getRecords().get(0).getValue()).isEqualTo("www.example.com.");
        }

        @Test
        @DisplayName("Should update RRSet")
        void shouldUpdateRRSet() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("PUT", "/v1/zones/42/rrsets/www/A", 200, """
                {
                  "rrset": {
                    "id": "www/A",
                    "name": "www",
                    "type": "A",
                    "ttl": 1200,
                    "labels": {
                      "updated": "true"
                    },
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "10.0.0.1",
                        "comment": "Updated IP address"
                      }
                    ],
                    "zone": 42
                  }
                }
                """);

            Record updatedRecord = new Record();
            updatedRecord.setValue("10.0.0.1");
            updatedRecord.setComment("Updated IP address");

            UpdateRRSetRequest request = new UpdateRRSetRequest(
                    "www",
                    1200L,
                    Map.of("updated", "true"),
                    List.of(updatedRecord)
            );

            RRSetResponse response = api.updateRRSet(42, "www/A", request);

            assertThat(response).isNotNull();
            assertThat(response.getRrset().getTtl()).isEqualTo(1200L);
            assertThat(response.getRrset().getRecords().get(0).getValue()).isEqualTo("10.0.0.1");
            assertThat(response.getRrset().getLabels()).containsEntry("updated", "true");
        }

        @Test
        @DisplayName("Should delete RRSet")
        void shouldDeleteRRSet() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("DELETE", "/v1/zones/42/rrsets/old/A", 200, """
                {
                  "action": {
                    "id": 789,
                    "command": "delete_rrset",
                    "status": "success",
                    "progress": 100,
                    "started": "2016-01-30T23:55:00+00:00",
                    "finished": "2016-01-30T23:56:00+00:00",
                    "resources": [],
                    "error": null
                  }
                }
                """);

            ActionResponse response = api.deleteRRSet(42, "old/A");

            assertThat(response).isNotNull();
            assertThat(response.getAction()).isNotNull();
            assertThat(response.getAction().getCommand()).isEqualTo("delete_rrset");
            assertThat(response.getAction().getStatus()).isEqualTo("success");
        }
    }

    @Nested
    @DisplayName("RRSet Actions")
    class RRSetActionsTests {

        @Test
        @DisplayName("Should change RRSet protection")
        void shouldChangeRRSetProtection() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones/42/rrsets/www/A/actions/change_protection", 201, """
                {
                  "action": {
                    "id": 1,
                    "command": "change_rrset_protection",
                    "status": "running",
                    "progress": 50,
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

            ActionResponse response = api.changeRRSetProtection(42, "www/A", true);

            assertThat(response).isNotNull();
            assertThat(response.getAction()).isNotNull();
            assertThat(response.getAction().getCommand()).isEqualTo("change_rrset_protection");
            assertThat(response.getAction().getStatus()).isEqualTo("running");
        }
    }

    @Nested
    @DisplayName("Advanced RRSet Types")
    class AdvancedRRSetTypesTests {

        @Test
        @DisplayName("Should handle SRV record RRSet")
        void shouldHandleSRVRecord() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets/_sip._tcp/SRV", 200, """
                {
                  "rrset": {
                    "id": "_sip._tcp/SRV",
                    "name": "_sip._tcp",
                    "type": "SRV",
                    "ttl": 3600,
                    "labels": {
                      "service": "sip"
                    },
                    "protection": {
                      "change": false
                    },
                    "records": [
                      {
                        "value": "10 60 5060 sip1.example.com.",
                        "comment": "Primary SIP server"
                      },
                      {
                        "value": "20 40 5060 sip2.example.com.",
                        "comment": "Backup SIP server"
                      }
                    ],
                    "zone": 42
                  }
                }
                """);

            RRSetResponse response = api.getRRSet(42, "_sip._tcp/SRV");

            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.SRV);
            assertThat(response.getRrset().getName()).isEqualTo("_sip._tcp");
            assertThat(response.getRrset().getRecords()).hasSize(2);
            assertThat(response.getRrset().getRecords().get(0).getValue()).contains("5060");
        }

        @Test
        @DisplayName("Should handle CAA record RRSet")
        void shouldHandleCAARecord() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets/@/CAA", 200, """
                {
                  "rrset": {
                    "id": "@/CAA",
                    "name": "@",
                    "type": "CAA",
                    "ttl": 86400,
                    "labels": {
                      "security": "ssl"
                    },
                    "protection": {
                      "change": true
                    },
                    "records": [
                      {
                        "value": "0 issue \\"letsencrypt.org\\"",
                        "comment": "Allow Let's Encrypt"
                      },
                      {
                        "value": "0 iodef \\"mailto:security@example.com\\"",
                        "comment": "Security contact"
                      }
                    ],
                    "zone": 42
                  }
                }
                """);

            RRSetResponse response = api.getRRSet(42, "@/CAA");

            assertThat(response.getRrset().getType()).isEqualTo(RRSetType.CAA);
            assertThat(response.getRrset().getRecords()).hasSize(2);
            assertThat(response.getRrset().getRecords().get(0).getValue()).contains("letsencrypt.org");
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle RRSet not found")
        void shouldHandleRRSetNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/zones/42/rrsets/nonexistent/A", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "RRSet not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getRRSet(42, "nonexistent/A"))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should handle invalid RRSet creation")
        void shouldHandleInvalidRRSetCreation() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/zones/42/rrsets", 422, """
                {
                  "error": {
                    "code": "invalid_input",
                    "message": "invalid record value",
                    "details": {
                      "fields": [
                        {
                          "name": "records.0.value",
                          "messages": ["is not a valid IP address"]
                        }
                      ]
                    }
                  }
                }
                """);

            Record invalidRecord = new Record();
            invalidRecord.setValue("not-an-ip");

            CreateRRSetRequest request = new CreateRRSetRequest(
                    "test",
                    RRSetType.A,
                    3600L,
                    Map.of(),
                    List.of(invalidRecord)
            );

            assertThatThrownBy(() -> api.createRRSet(42, request))
                    .isInstanceOf(Exception.class);
        }
    }
}