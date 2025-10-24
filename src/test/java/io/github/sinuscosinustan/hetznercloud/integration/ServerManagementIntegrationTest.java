package io.github.sinuscosinustan.hetznercloud.integration;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.enums.ServerType;
import io.github.sinuscosinustan.hetznercloud.objects.request.CreateServerRequest;
import io.github.sinuscosinustan.hetznercloud.objects.request.ServerPublicNetRequest;
import io.github.sinuscosinustan.hetznercloud.objects.request.CreateSSHKeyRequest;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.jclouds.ssh.SshKeys;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class ServerManagementIntegrationTest {

    private HetznerCloudAPI hetznerCloudAPI;
    private final String testUUID = UUID.randomUUID().toString();
    private final String testUUIDLabelKey = "java/testID";
    private final String testUUIDLabelSelector = String.format("%s=%s", testUUIDLabelKey, testUUID);

    @BeforeAll
    void setUp() {
        String token = System.getenv("HCLOUD_TOKEN");
        assumeTrue(token != null && !token.isBlank(), "HCLOUD_TOKEN environment variable is required for integration tests");

        hetznerCloudAPI = new HetznerCloudAPI(token);
    }

    @AfterAll
    void cleanUp() {
        if (hetznerCloudAPI == null) return;

        log.info("Cleaning up integration test resources");

        try {
            // Clean up servers
            var servers = hetznerCloudAPI.getServers(testUUIDLabelSelector).getServers();
            log.info("Found {} servers to clean up", servers.size());

            servers.forEach(server -> {
                try {
                    log.info("Cleaning up server: {}", server.getName());

                    // Wait for any pending actions
                    var actions = hetznerCloudAPI.getServerActions(server.getId()).getActions();
                    actions.forEach(action -> {
                        try {
                            log.info("Waiting for action '{}' to finish for server '{}'", action.getCommand(), server.getName());
                            Awaitility.await()
                                    .atMost(Duration.ofSeconds(30))
                                    .pollInterval(Duration.ofSeconds(2))
                                    .until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
                        } catch (Exception e) {
                            log.warn("Failed to wait for action {} completion: {}", action.getId(), e.getMessage());
                        }
                    });

                    log.info("Deleting server '{}'", server.getName());
                    var deleteAction = hetznerCloudAPI.deleteServer(server.getId());

                    // Wait for server deletion to complete
                    if (deleteAction != null && deleteAction.getAction() != null) {
                        log.info("Waiting for server deletion to complete");
                        Awaitility.await()
                                .atMost(Duration.ofSeconds(60))
                                .pollInterval(Duration.ofSeconds(3))
                                .until(() -> hetznerCloudAPI.getAction(deleteAction.getAction().getId()).getAction().getFinished() != null);
                    }
                    log.info("Successfully deleted server: {}", server.getName());
                } catch (Exception e) {
                    log.error("Failed to delete server {}: {}", server.getName(), e.getMessage());
                }
            });

            // Clean up SSH keys
            var sshKeys = hetznerCloudAPI.getSSHKeys(testUUIDLabelSelector).getSshKeys();
            log.info("Found {} SSH keys to clean up", sshKeys.size());

            sshKeys.forEach(sshKey -> {
                try {
                    log.info("Deleting SSH key '{}'", sshKey.getName());
                    hetznerCloudAPI.deleteSSHKey(sshKey.getId());
                    log.info("Successfully deleted SSH key: {}", sshKey.getName());
                } catch (Exception e) {
                    log.error("Failed to delete SSH key {}: {}", sshKey.getName(), e.getMessage());
                }
            });

            log.info("Cleanup completed");
        } catch (Exception e) {
            log.error("Cleanup failed with error: {}", e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("Should create and manage server lifecycle")
    void shouldCreateAndManageServerLifecycle() {
        // Generate SSH key for the test
        Map<String, String> keyPair = SshKeys.generate();
        String keyId = UUID.randomUUID().toString();

        // Create SSH key
        var createdKey = hetznerCloudAPI.createSSHKey(
                CreateSSHKeyRequest.builder()
                        .name(keyId)
                        .publicKey(keyPair.get("public"))
                        .label(testUUIDLabelKey, testUUID)
                        .build());

        assertThat(createdKey).isNotNull();
        assertThat(createdKey.getSshKey().getPublicKey()).isEqualTo(keyPair.get("public"));

        // Create server
        String serverName = UUID.randomUUID().toString();
        var createServer = hetznerCloudAPI.createServer(
                CreateServerRequest.builder()
                        .name(serverName)
                        .serverType(ServerType.cpx12.name())
                        .publicNet(ServerPublicNetRequest.builder()
                                .enableIPv4(false)
                                .enableIPv6(true)
                                .build())
                        .image("ubuntu-22.04")
                        .label(testUUIDLabelKey, testUUID)
                        .sshKey(createdKey.getSshKey().getId())
                        .build());

        assertThat(createServer).isNotNull();
        assertThat(createServer.getServer().getName()).isEqualTo(serverName);

        // Wait for server creation to complete
        hetznerCloudAPI.getServerActions(createServer.getServer().getId()).getActions().forEach(action -> {
            Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
        });

        // Test server operations
        var poweredOffServer = hetznerCloudAPI.powerOffServer(createServer.getServer().getId());
        assertThat(poweredOffServer.getAction()).isNotNull();

        // Wait for power off
        Awaitility.await().until(() -> hetznerCloudAPI.getAction(poweredOffServer.getAction().getId()).getAction().getFinished() != null);

        var poweredOnServer = hetznerCloudAPI.powerOnServer(createServer.getServer().getId());
        assertThat(poweredOnServer.getAction()).isNotNull();

        // Verify server exists in list
        var servers = hetznerCloudAPI.getServers(testUUIDLabelSelector);
        assertThat(servers.getServers()).hasSize(1);
        assertThat(servers.getServers().get(0).getName()).isEqualTo(serverName);
    }

    @Test
    @DisplayName("Should handle server reset operation")
    void shouldHandleServerResetOperation() {
        // This test assumes there's at least one test server from previous test
        var servers = hetznerCloudAPI.getServers(testUUIDLabelSelector);
        assumeTrue(!servers.getServers().isEmpty(), "No test servers available for reset test");

        var server = servers.getServers().get(0);

        // Wait for any pending actions
        hetznerCloudAPI.getServerActions(server.getId()).getActions().forEach(action -> {
            if (action.getFinished() == null) {
                Awaitility.await().until(() -> hetznerCloudAPI.getAction(action.getId()).getAction().getFinished() != null);
            }
        });

        var resetAction = hetznerCloudAPI.resetServer(server.getId());
        assertThat(resetAction.getAction()).isNotNull();
        assertThat(hetznerCloudAPI.getAction(resetAction.getAction().getId()).getAction().getStatus())
                .isIn("success", "running");
    }

    @Test
    @DisplayName("Should retrieve server metrics")
    void shouldRetrieveServerMetrics() {
        var servers = hetznerCloudAPI.getServers(testUUIDLabelSelector);
        assumeTrue(!servers.getServers().isEmpty(), "No test servers available for metrics test");

        var server = servers.getServers().get(0);

        // Get metrics for the last hour
        var endTime = java.time.Instant.now();
        var startTime = endTime.minus(1, java.time.temporal.ChronoUnit.HOURS);

        var metrics = hetznerCloudAPI.getServerMetrics(
                server.getId(),
                "cpu",
                hetznerCloudAPI.convertToISO8601(java.util.Date.from(startTime)),
                hetznerCloudAPI.convertToISO8601(java.util.Date.from(endTime))
        );

        assertThat(metrics).isNotNull();
        assertThat(metrics.getMetrics()).isNotNull();
    }
}
