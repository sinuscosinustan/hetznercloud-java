package io.github.sinuscosinustan.hetznercloud.unit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class OpenAPIMockServer {

    protected WireMockServer wireMockServer;
    protected String baseUrl;
    protected OpenAPI cloudApiSpec;
    protected OpenAPI hetznerApiSpec;
    protected boolean strictMode = false;  // When true, only use auto-generated mocks

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

        // Generate mocks from OpenAPI specs if available
        generateMocksFromSpecs();
    }

    @AfterEach
    public void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    private void loadOpenAPISpecs() {
        try {
            // Load the real OpenAPI specifications
            OpenAPIV3Parser parser = new OpenAPIV3Parser();

            cloudApiSpec = parser.read("build/cloud.spec.json");
            hetznerApiSpec = parser.read("build/hetzner.spec.json");
        } catch (Exception e) {
            cloudApiSpec = new OpenAPI();
            hetznerApiSpec = new OpenAPI();
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
     * Enable strict mode - only use auto-generated mocks from OpenAPI specs
     */
    protected void enableStrictMode() {
        this.strictMode = true;
    }

    /**
     * Add a custom stub for testing specific scenarios
     */
    protected void addStub(String method, String urlPattern, int status, String responseBody) {
        if (strictMode) {
            System.out.println("Manual stubs not allowed in strict mode. Use OpenAPI-generated mocks only: " + method + " " + urlPattern);
            return;
        }
        // Parse URL pattern to separate path and query parameters
        var mappingBuilder = switch (method.toUpperCase()) {
            case "GET" -> get(urlPathEqualTo(getPathPattern(urlPattern)));
            case "POST" -> post(urlPathEqualTo(getPathPattern(urlPattern)));
            case "PATCH" -> patch(urlPathEqualTo(getPathPattern(urlPattern)));
            case "PUT" -> put(urlPathEqualTo(getPathPattern(urlPattern)));
            case "DELETE" -> delete(urlPathEqualTo(getPathPattern(urlPattern)));
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

    /**
     * Get the loaded Cloud API specification
     */
    protected OpenAPI getCloudApiSpec() {
        return cloudApiSpec;
    }

    /**
     * Get the loaded Hetzner API specification
     */
    protected OpenAPI getHetznerApiSpec() {
        return hetznerApiSpec;
    }

    /**
     * Generate mock stubs from loaded OpenAPI specifications
     */
    private void generateMocksFromSpecs() {
        if (cloudApiSpec != null && cloudApiSpec.getPaths() != null) {
            generateMocksFromSpec(cloudApiSpec);
        }
        if (hetznerApiSpec != null && hetznerApiSpec.getPaths() != null) {
            generateMocksFromSpec(hetznerApiSpec);
        }
    }

    /**
     * Generate mock stubs from a single OpenAPI specification
     */
    private void generateMocksFromSpec(OpenAPI spec) {
        for (Map.Entry<String, PathItem> pathEntry : spec.getPaths().entrySet()) {
            String path = pathEntry.getKey();
            PathItem pathItem = pathEntry.getValue();

            // Generate mocks for each HTTP method
            if (pathItem.getGet() != null) {
                generateMockForOperation(path, "GET", pathItem.getGet());
            }
            if (pathItem.getPost() != null) {
                generateMockForOperation(path, "POST", pathItem.getPost());
            }
            if (pathItem.getPatch() != null) {
                generateMockForOperation(path, "PATCH", pathItem.getPatch());
            }
            if (pathItem.getPut() != null) {
                generateMockForOperation(path, "PUT", pathItem.getPut());
            }
            if (pathItem.getDelete() != null) {
                generateMockForOperation(path, "DELETE", pathItem.getDelete());
            }
        }
    }

    /**
     * Generate a mock stub for a specific operation
     */
    private void generateMockForOperation(String path, String method, Operation operation) {
        if (operation.getResponses() == null) return;

        // Look for 200/201 success responses
        ApiResponse successResponse = operation.getResponses().get("200");
        if (successResponse == null) {
            successResponse = operation.getResponses().get("201");
        }
        if (successResponse == null) return;

        // Get the response content
        if (successResponse.getContent() == null) return;

        MediaType jsonContent = successResponse.getContent().get("application/json");
        if (jsonContent == null) return;

        // Try to get an example response
        String responseBody = getExampleFromMediaType(jsonContent);
        if (responseBody == null) return;

        // Create the mock stub
        var mappingBuilder = switch (method.toUpperCase()) {
            case "GET" -> get(urlPathEqualTo(path));
            case "POST" -> post(urlPathEqualTo(path));
            case "PATCH" -> patch(urlPathEqualTo(path));
            case "PUT" -> put(urlPathEqualTo(path));
            case "DELETE" -> delete(urlPathEqualTo(path));
            default -> null;
        };

        if (mappingBuilder != null) {
            // Determine status code
            int statusCode = "POST".equals(method) ? 201 : 200;

            wireMockServer.stubFor(mappingBuilder
                    .atPriority(1)  // High priority - overrides manual stubs
                    .willReturn(aResponse()
                            .withStatus(statusCode)
                            .withHeader("Content-Type", "application/json")
                            .withBody(responseBody)));

            System.out.println("Generated mock for " + method + " " + path);
        }
    }

    /**
     * Extract example JSON from MediaType
     */
    private String getExampleFromMediaType(MediaType mediaType) {
        // Try to get example from examples
        if (mediaType.getExamples() != null && !mediaType.getExamples().isEmpty()) {
            Example example = mediaType.getExamples().values().iterator().next();
            if (example.getValue() != null) {
                return example.getValue().toString();
            }
        }

        // Try to get example from example field
        if (mediaType.getExample() != null) {
            return mediaType.getExample().toString();
        }

        // Try to get example from schema
        if (mediaType.getSchema() != null && mediaType.getSchema().getExample() != null) {
            return mediaType.getSchema().getExample().toString();
        }

        return null;
    }

    /**
     * Add a custom stub that overrides spec-generated mocks
     */
    protected void addStubWithPriority(String method, String urlPattern, int status, String responseBody, int priority) {
        var mappingBuilder = switch (method.toUpperCase()) {
            case "GET" -> get(urlPathEqualTo(getPathPattern(urlPattern)));
            case "POST" -> post(urlPathEqualTo(getPathPattern(urlPattern)));
            case "PATCH" -> patch(urlPathEqualTo(getPathPattern(urlPattern)));
            case "PUT" -> put(urlPathEqualTo(getPathPattern(urlPattern)));
            case "DELETE" -> delete(urlPathEqualTo(getPathPattern(urlPattern)));
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
                .atPriority(priority)  // Higher priority overrides spec-generated mocks
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }
}
