package io.github.sinuscosinustan.hetznercloud.unit.loadbalancers;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.response.*;
import io.github.sinuscosinustan.hetznercloud.unit.OpenAPIMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

class LoadBalancerTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("Load Balancer Retrieval")
    class LoadBalancerRetrievalTests {

        @Test
        @DisplayName("Should get single load balancer by ID")
        void shouldGetLoadBalancerById() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancers/1", 200, """
                {
                  "load_balancer": {
                    "id": 1,
                    "name": "my-loadbalancer",
                    "public_net": {
                      "enabled": true,
                      "ipv4": {
                        "ip": "192.168.1.1"
                      },
                      "ipv6": {
                        "ip": "2001:db8::/64"
                      }
                    },
                    "private_net": [],
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
                    "load_balancer_type": {
                      "id": 1,
                      "name": "lb11",
                      "description": "LB-11",
                      "max_connections": 5000,
                      "max_assigned_certificates": 10,
                      "max_services": 5,
                      "max_targets": 25
                    },
                    "protection": {
                      "delete": false
                    },
                    "labels": {},
                    "algorithm": {
                      "type": "round_robin"
                    },
                    "services": [],
                    "targets": []
                  }
                }
                """);

            LoadBalancerResponse response = api.getLoadBalancer(1);

            assertThat(response).isNotNull();
            assertThat(response.getLoadBalancer()).isNotNull();
            assertThat(response.getLoadBalancer().getId()).isEqualTo(1L);
            assertThat(response.getLoadBalancer().getName()).isEqualTo("my-loadbalancer");
            assertThat(response.getLoadBalancer().getPublicIpv4()).isNotNull();
            assertThat(response.getLoadBalancer().getPublicIpv6()).isNotNull();
            assertThat(response.getLoadBalancer().getLocation()).isNotNull();
            assertThat(response.getLoadBalancer().getLocation().getName()).isEqualTo("fsn1");
            assertThat(response.getLoadBalancer().getLoadBalancerType()).isNotNull();
            assertThat(response.getLoadBalancer().getLoadBalancerType().getName()).isEqualTo("lb11");
        }

        @Test
        @DisplayName("Should get all load balancers")
        void shouldGetAllLoadBalancers() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancers", 200, """
                {
                  "load_balancers": [
                    {
                      "id": 1,
                      "name": "my-loadbalancer-1",
                      "public_net": {
                        "enabled": true,
                        "ipv4": {
                          "ip": "192.168.1.1"
                        },
                        "ipv6": {
                          "ip": "2001:db8::/64"
                        }
                      },
                      "private_net": [],
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
                      "load_balancer_type": {
                        "id": 1,
                        "name": "lb11"
                      },
                      "protection": {
                        "delete": false
                      },
                      "labels": {},
                      "algorithm": {
                        "type": "round_robin"
                      },
                      "services": [],
                      "targets": []
                    },
                    {
                      "id": 2,
                      "name": "my-loadbalancer-2",
                      "public_net": {
                        "enabled": true,
                        "ipv4": {
                          "ip": "192.168.1.2"
                        },
                        "ipv6": {
                          "ip": "2001:db8::/65"
                        }
                      },
                      "private_net": [],
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
                      "load_balancer_type": {
                        "id": 2,
                        "name": "lb21"
                      },
                      "protection": {
                        "delete": false
                      },
                      "labels": {},
                      "algorithm": {
                        "type": "least_connections"
                      },
                      "services": [],
                      "targets": []
                    }
                  ]
                }
                """);

            LoadBalancersResponse response = api.getLoadBalancers();

            assertThat(response).isNotNull();
            assertThat(response.getLoadBalancers()).isNotNull().hasSize(2);

            var lb1 = response.getLoadBalancers().get(0);
            assertThat(lb1.getId()).isEqualTo(1L);
            assertThat(lb1.getName()).isEqualTo("my-loadbalancer-1");

            var lb2 = response.getLoadBalancers().get(1);
            assertThat(lb2.getId()).isEqualTo(2L);
            assertThat(lb2.getName()).isEqualTo("my-loadbalancer-2");
        }

        @Test
        @DisplayName("Should get load balancer by name")
        void shouldGetLoadBalancerByName() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancers?name=my-loadbalancer", 200, """
                {
                  "load_balancers": [
                    {
                      "id": 1,
                      "name": "my-loadbalancer",
                      "public_net": {
                        "enabled": true,
                        "ipv4": {
                          "ip": "192.168.1.1"
                        },
                        "ipv6": {
                          "ip": "2001:db8::/64"
                        }
                      },
                      "private_net": [],
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
                      "load_balancer_type": {
                        "id": 1,
                        "name": "lb11"
                      },
                      "protection": {
                        "delete": false
                      },
                      "labels": {},
                      "algorithm": {
                        "type": "round_robin"
                      },
                      "services": [],
                      "targets": []
                    }
                  ]
                }
                """);

            LoadBalancersResponse response = api.getLoadBalancerByName("my-loadbalancer");

            assertThat(response).isNotNull();
            assertThat(response.getLoadBalancers()).isNotNull().hasSize(1);
            assertThat(response.getLoadBalancers().get(0).getName()).isEqualTo("my-loadbalancer");
        }
    }

    @Nested
    @DisplayName("Load Balancer Types")
    class LoadBalancerTypesTests {

        @Test
        @DisplayName("Should get all load balancer types")
        void shouldGetAllLoadBalancerTypes() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancer_types", 200, """
                {
                  "load_balancer_types": [
                    {
                      "id": 1,
                      "name": "lb11",
                      "description": "LB-11",
                      "max_connections": 5000,
                      "max_assigned_certificates": 10,
                      "max_services": 5,
                      "max_targets": 25,
                      "prices": []
                    },
                    {
                      "id": 2,
                      "name": "lb21",
                      "description": "LB-21",
                      "max_connections": 10000,
                      "max_assigned_certificates": 10,
                      "max_services": 10,
                      "max_targets": 50,
                      "prices": []
                    }
                  ]
                }
                """);

            LoadBalancerTypesResponse response = api.getLoadBalancerTypes();

            assertThat(response).isNotNull();
            assertThat(response.getLoadBalancerTypes()).isNotNull().hasSize(2);

            var type1 = response.getLoadBalancerTypes().get(0);
            assertThat(type1.getId()).isEqualTo(1L);
            assertThat(type1.getName()).isEqualTo("lb11");
            assertThat(type1.getMaxConnections()).isEqualTo(5000);

            var type2 = response.getLoadBalancerTypes().get(1);
            assertThat(type2.getId()).isEqualTo(2L);
            assertThat(type2.getName()).isEqualTo("lb21");
            assertThat(type2.getMaxConnections()).isEqualTo(10000);
        }

        @Test
        @DisplayName("Should get load balancer type by ID")
        void shouldGetLoadBalancerTypeById() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancer_types/1", 200, """
                {
                  "load_balancer_type": {
                    "id": 1,
                    "name": "lb11",
                    "description": "LB-11",
                    "max_connections": 5000,
                    "max_assigned_certificates": 10,
                    "max_services": 5,
                    "max_targets": 25,
                    "prices": []
                  }
                }
                """);

            LoadBalancerTypeResponse response = api.getLoadBalancerType(1);

            assertThat(response).isNotNull();
            assertThat(response.getLoadBalancerType()).isNotNull();
            assertThat(response.getLoadBalancerType().getId()).isEqualTo(1L);
            assertThat(response.getLoadBalancerType().getName()).isEqualTo("lb11");
            assertThat(response.getLoadBalancerType().getDescription()).isEqualTo("LB-11");
        }

        @Test
        @DisplayName("Should get load balancer type by name")
        void shouldGetLoadBalancerTypeByName() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancer_types?name=lb11", 200, """
                {
                  "load_balancer_types": [
                    {
                      "id": 1,
                      "name": "lb11",
                      "description": "LB-11",
                      "max_connections": 5000,
                      "max_assigned_certificates": 10,
                      "max_services": 5,
                      "max_targets": 25,
                      "prices": []
                    }
                  ]
                }
                """);

            LoadBalancerTypesResponse response = api.getLoadBalancerTypeByName("lb11");

            assertThat(response).isNotNull();
            assertThat(response.getLoadBalancerTypes()).isNotNull().hasSize(1);
            assertThat(response.getLoadBalancerTypes().get(0).getName()).isEqualTo("lb11");
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle load balancer not found")
        void shouldHandleLoadBalancerNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancers/999", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "Load balancer not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getLoadBalancer(999))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should handle load balancer type not found")
        void shouldHandleLoadBalancerTypeNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/load_balancer_types/999", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "Load balancer type not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getLoadBalancerType(999))
                    .isInstanceOf(Exception.class);
        }
    }
}