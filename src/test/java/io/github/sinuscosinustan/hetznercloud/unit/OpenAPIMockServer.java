package io.github.sinuscosinustan.hetznercloud.unit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class OpenAPIMockServer {

    protected WireMockServer wireMockServer;
    protected String baseUrl;
    protected OpenAPI cloudApiSpec;
    protected OpenAPI hetznerApiSpec;

    @BeforeEach
    public void setUp() throws Exception {
        // Start WireMock server
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        baseUrl = "http://localhost:" + wireMockServer.port();

        // Load OpenAPI specifications
        loadOpenAPISpecs();

        // Configure basic mock responses
        configureMockResponses();
    }

    @AfterEach
    public void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private void loadOpenAPISpecs() {
        try {
            // For now, we'll create basic specs, but in a real implementation
            // you would load from https://docs.hetzner.cloud/cloud.spec.json
            cloudApiSpec = new OpenAPI();
            hetznerApiSpec = new OpenAPI();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load OpenAPI specifications", e);
        }
    }

    private void configureMockResponses() {
        // Configure basic successful responses for common endpoints

        // Server endpoints
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/servers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "servers": [],
                                  "meta": {
                                    "pagination": {
                                      "page": 1,
                                      "per_page": 25,
                                      "previous_page": null,
                                      "next_page": null,
                                      "last_page": 1,
                                      "total_entries": 0
                                    }
                                  }
                                }
                                """)));

        // Server types endpoint with new structure
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/server_types"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "server_types": [
                                    {
                                      "id": 1,
                                      "name": "cx11",
                                      "description": "CX11",
                                      "cores": 1,
                                      "memory": 4.0,
                                      "disk": 25,
                                      "deprecated": null,
                                      "prices": [],
                                      "storage_type": "local",
                                      "cpu_type": "shared",
                                      "architecture": "x86",
                                      "included_traffic": 654321,
                                      "category": "shared vCPU",
                                      "locations": [
                                        {
                                          "id": 1,
                                          "name": "fsn1",
                                          "deprecation": null
                                        }
                                      ]
                                    }
                                  ]
                                }
                                """)));

        // Actions endpoint
        wireMockServer.stubFor(get(urlPathMatching("/v1/actions.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "actions": [],
                                  "meta": {
                                    "pagination": {
                                      "page": 1,
                                      "per_page": 25,
                                      "previous_page": null,
                                      "next_page": null,
                                      "last_page": 1,
                                      "total_entries": 0
                                    }
                                  }
                                }
                                """)));

        // Error responses
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/servers/999999"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "error": {
                                    "code": "not_found",
                                    "message": "Server not found"
                                  }
                                }
                                """)));
    }

    /**
     * Get the base URL for the mock server, suitable for use as API base URL
     */
    public String getMockApiUrl() {
        return baseUrl + "/v1";
    }

    /**
     * Reset all stubs to default state
     */
    protected void resetToDefaults() {
        wireMockServer.resetAll();
        configureMockResponses();
    }

    /**
     * Add a custom stub for testing specific scenarios
     */
    protected void addStub(String method, String urlPattern, int status, String responseBody) {
        // Parse URL pattern to separate path and query parameters
        var mappingBuilder = switch (method.toUpperCase()) {
            case "GET" -> get(urlPathMatching(getPathPattern(urlPattern)));
            case "POST" -> post(urlPathMatching(getPathPattern(urlPattern)));
            case "PUT" -> put(urlPathMatching(getPathPattern(urlPattern)));
            case "DELETE" -> delete(urlPathMatching(getPathPattern(urlPattern)));
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };

        // Add query parameter matching if present
        if (urlPattern.contains("?")) {
            String[] parts = urlPattern.split("\\?", 2);
            String[] queryParams = parts[1].split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    mappingBuilder = mappingBuilder.withQueryParam(keyValue[0], equalTo(keyValue[1]));
                }
            }
        }

        wireMockServer.stubFor(mappingBuilder
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }

    private String getPathPattern(String urlPattern) {
        // Extract just the path part without query parameters
        if (urlPattern.contains("?")) {
            return urlPattern.split("\\?")[0];
        }
        return urlPattern;
    }
}