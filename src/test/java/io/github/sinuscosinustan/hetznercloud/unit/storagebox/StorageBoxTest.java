package io.github.sinuscosinustan.hetznercloud.unit.storagebox;

import io.github.sinuscosinustan.hetznercloud.HetznerCloudAPI;
import io.github.sinuscosinustan.hetznercloud.objects.request.CreateStorageBoxRequest;
import io.github.sinuscosinustan.hetznercloud.objects.request.UpdateStorageBoxRequest;
import io.github.sinuscosinustan.hetznercloud.objects.response.StorageBoxResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.StorageBoxesResponse;
import io.github.sinuscosinustan.hetznercloud.objects.response.StorageBoxSnapshotsResponse;
import io.github.sinuscosinustan.hetznercloud.unit.OpenAPIMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class StorageBoxTest extends OpenAPIMockServer {

    @Nested
    @DisplayName("Storage Box Retrieval")
    class StorageBoxRetrievalTests {

        @Test
        @DisplayName("Should get single Storage Box by ID")
        void shouldGetStorageBoxById() {
            // Use mock server URL for Storage Box testing
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/storage_boxes/1", 200, """
                {
                  "storage_box": {
                    "id": 1,
                    "name": "my-storage-box",
                    "location": "fsn1",
                    "product": "BX10",
                    "disk_quota": 107374182400,
                    "disk_usage": 1073741824,
                    "server_ip": "192.168.1.100",
                    "webdav_url": "https://u12345.your-storagebox.de",
                    "samba_url": "//u12345.your-storagebox.de/backup",
                    "ssh_url": "u12345@u12345.your-storagebox.de",
                    "ftp_url": "ftp://u12345.your-storagebox.de",
                    "created": "2023-01-01T00:00:00Z",
                    "cancelled": null,
                    "locked": false,
                    "ssh_support": true,
                    "webdav_support": true,
                    "samba_support": true,
                    "ftp_support": true,
                    "readonly_access": false,
                    "writeable_access": true,
                    "snapshot_plan": {
                      "enabled": true,
                      "hour_of_day": 2,
                      "timezone": "UTC",
                      "keep_hourly": 24,
                      "keep_daily": 7,
                      "keep_weekly": 4,
                      "keep_monthly": 12
                    },
                    "labels": {
                      "environment": "production"
                    },
                    "sub_accounts": []
                  }
                }
                """);

            StorageBoxResponse response = api.getStorageBox(1);

            assertThat(response).isNotNull();
            assertThat(response.getStorageBox()).isNotNull();
            assertThat(response.getStorageBox().getId()).isEqualTo(1L);
            assertThat(response.getStorageBox().getName()).isEqualTo("my-storage-box");
            assertThat(response.getStorageBox().getLocation()).isEqualTo("fsn1");
            assertThat(response.getStorageBox().getProduct()).isEqualTo("BX10");
            assertThat(response.getStorageBox().getDiskQuota()).isEqualTo(107374182400L);
            assertThat(response.getStorageBox().getSshSupport()).isTrue();
            assertThat(response.getStorageBox().getSnapshotPlan()).isNotNull();
            assertThat(response.getStorageBox().getSnapshotPlan().getEnabled()).isTrue();
            assertThat(response.getStorageBox().getLabels()).containsEntry("environment", "production");
        }

        @Test
        @DisplayName("Should get all Storage Boxes")
        void shouldGetAllStorageBoxes() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/storage_boxes", 200, """
                {
                  "storage_boxes": [
                    {
                      "id": 1,
                      "name": "storage-box-1",
                      "location": "fsn1",
                      "product": "BX10",
                      "disk_quota": 107374182400,
                      "disk_usage": 1073741824,
                      "server_ip": "192.168.1.100",
                      "created": "2023-01-01T00:00:00Z",
                      "ssh_support": true,
                      "labels": {}
                    },
                    {
                      "id": 2,
                      "name": "storage-box-2",
                      "location": "nbg1",
                      "product": "BX20",
                      "disk_quota": 214748364800,
                      "disk_usage": 2147483648,
                      "server_ip": "192.168.1.101",
                      "created": "2023-01-02T00:00:00Z",
                      "ssh_support": true,
                      "labels": {}
                    }
                  ]
                }
                """);

            StorageBoxesResponse response = api.getStorageBoxes();

            assertThat(response).isNotNull();
            assertThat(response.getStorageBoxes()).isNotNull().hasSize(2);

            var box1 = response.getStorageBoxes().get(0);
            assertThat(box1.getId()).isEqualTo(1L);
            assertThat(box1.getName()).isEqualTo("storage-box-1");
            assertThat(box1.getLocation()).isEqualTo("fsn1");

            var box2 = response.getStorageBoxes().get(1);
            assertThat(box2.getId()).isEqualTo(2L);
            assertThat(box2.getName()).isEqualTo("storage-box-2");
            assertThat(box2.getLocation()).isEqualTo("nbg1");
        }

        @Test
        @DisplayName("Should get Storage Box by name")
        void shouldGetStorageBoxByName() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/storage_boxes?name=my-storage-box", 200, """
                {
                  "storage_boxes": [
                    {
                      "id": 1,
                      "name": "my-storage-box",
                      "location": "fsn1",
                      "product": "BX10",
                      "disk_quota": 107374182400,
                      "disk_usage": 1073741824,
                      "server_ip": "192.168.1.100",
                      "created": "2023-01-01T00:00:00Z",
                      "ssh_support": true,
                      "labels": {}
                    }
                  ]
                }
                """);

            StorageBoxesResponse response = api.getStorageBoxByName("my-storage-box");

            assertThat(response).isNotNull();
            assertThat(response.getStorageBoxes()).isNotNull().hasSize(1);
            assertThat(response.getStorageBoxes().get(0).getName()).isEqualTo("my-storage-box");
        }
    }

    @Nested
    @DisplayName("Storage Box Management")
    class StorageBoxManagementTests {

        @Test
        @DisplayName("Should create Storage Box")
        void shouldCreateStorageBox() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("POST", "/v1/storage_boxes", 201, """
                {
                  "storage_box": {
                    "id": 1,
                    "name": "new-storage-box",
                    "location": "fsn1",
                    "product": "BX10",
                    "disk_quota": 107374182400,
                    "disk_usage": 0,
                    "server_ip": "192.168.1.100",
                    "created": "2023-01-01T00:00:00Z",
                    "ssh_support": true,
                    "webdav_support": true,
                    "samba_support": false,
                    "ftp_support": false,
                    "readonly_access": false,
                    "writeable_access": true,
                    "labels": {
                      "project": "test"
                    }
                  },
                  "actions": []
                }
                """);

            CreateStorageBoxRequest request = new CreateStorageBoxRequest(
                    "new-storage-box",
                    "BX10",
                    "fsn1",
                    true,  // SSH support
                    true,  // WebDAV support
                    false, // Samba support
                    false, // FTP support
                    false, // readonly access
                    true,  // writeable access
                    null,  // snapshot plan
                    Map.of("project", "test")
            );

            StorageBoxResponse response = api.createStorageBox(request);

            assertThat(response).isNotNull();
            assertThat(response.getStorageBox()).isNotNull();
            assertThat(response.getStorageBox().getName()).isEqualTo("new-storage-box");
            assertThat(response.getStorageBox().getProduct()).isEqualTo("BX10");
            assertThat(response.getStorageBox().getSshSupport()).isTrue();
            assertThat(response.getStorageBox().getWebdavSupport()).isTrue();
            assertThat(response.getStorageBox().getSambaSupport()).isFalse();
        }

        @Test
        @DisplayName("Should update Storage Box")
        void shouldUpdateStorageBox() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("PUT", "/v1/storage_boxes/1", 200, """
                {
                  "storage_box": {
                    "id": 1,
                    "name": "updated-storage-box",
                    "location": "fsn1",
                    "product": "BX10",
                    "backup_email": "admin@example.com",
                    "ssh_support": false,
                    "webdav_support": true,
                    "labels": {
                      "project": "updated"
                    }
                  }
                }
                """);

            UpdateStorageBoxRequest request = new UpdateStorageBoxRequest(
                    "updated-storage-box",
                    "admin@example.com",
                    false, // SSH support disabled
                    true,  // WebDAV support
                    null, null, null, null, null,
                    Map.of("project", "updated")
            );

            StorageBoxResponse response = api.updateStorageBox(1, request);

            assertThat(response).isNotNull();
            assertThat(response.getStorageBox()).isNotNull();
            assertThat(response.getStorageBox().getName()).isEqualTo("updated-storage-box");
            assertThat(response.getStorageBox().getBackupEmail()).isEqualTo("admin@example.com");
            assertThat(response.getStorageBox().getSshSupport()).isFalse();
        }
    }

    @Nested
    @DisplayName("Storage Box Snapshots")
    class StorageBoxSnapshotTests {

        @Test
        @DisplayName("Should get Storage Box snapshots")
        void shouldGetStorageBoxSnapshots() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/storage_boxes/1/snapshots", 200, """
                {
                  "snapshots": [
                    {
                      "name": "snapshot-2023-01-01",
                      "created": "2023-01-01T02:00:00Z",
                      "size": 1073741824,
                      "storage_box_id": 1
                    },
                    {
                      "name": "snapshot-2023-01-02",
                      "created": "2023-01-02T02:00:00Z",
                      "size": 1073741824,
                      "storage_box_id": 1
                    }
                  ]
                }
                """);

            StorageBoxSnapshotsResponse response = api.getStorageBoxSnapshots(1);

            assertThat(response).isNotNull();
            assertThat(response.getSnapshots()).isNotNull().hasSize(2);

            var snapshot1 = response.getSnapshots().get(0);
            assertThat(snapshot1.getName()).isEqualTo("snapshot-2023-01-01");
            assertThat(snapshot1.getStorageBoxId()).isEqualTo(1L);

            var snapshot2 = response.getSnapshots().get(1);
            assertThat(snapshot2.getName()).isEqualTo("snapshot-2023-01-02");
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle Storage Box not found")
        void shouldHandleStorageBoxNotFound() {
            HetznerCloudAPI api = new HetznerCloudAPI("test-token", getMockApiUrl());

            addStub("GET", "/v1/storage_boxes/999", 404, """
                {
                  "error": {
                    "code": "not_found",
                    "message": "Storage Box not found"
                  }
                }
                """);

            assertThatThrownBy(() -> api.getStorageBox(999))
                    .isInstanceOf(Exception.class);
        }
    }
}