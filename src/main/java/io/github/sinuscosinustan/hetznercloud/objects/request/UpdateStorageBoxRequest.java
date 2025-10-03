package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sinuscosinustan.hetznercloud.objects.general.StorageBoxSnapshotPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStorageBoxRequest {

    private String name;

    @JsonProperty("backup_email")
    private String backupEmail;

    @JsonProperty("ssh_support")
    private Boolean sshSupport;

    @JsonProperty("webdav_support")
    private Boolean webdavSupport;

    @JsonProperty("samba_support")
    private Boolean sambaSupport;

    @JsonProperty("ftp_support")
    private Boolean ftpSupport;

    @JsonProperty("readonly_access")
    private Boolean readonlyAccess;

    @JsonProperty("writeable_access")
    private Boolean writeableAccess;

    @JsonProperty("snapshot_plan")
    private StorageBoxSnapshotPlan snapshotPlan;

    private Map<String, String> labels;
}