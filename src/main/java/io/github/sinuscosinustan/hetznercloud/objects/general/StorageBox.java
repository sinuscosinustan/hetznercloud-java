package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class StorageBox {

    private Long id;
    private String name;
    private String location;
    private String product;

    @JsonProperty("disk_quota")
    private Long diskQuota;

    @JsonProperty("disk_usage")
    private Long diskUsage;

    @JsonProperty("server_ip")
    private String serverIp;

    @JsonProperty("webdav_url")
    private String webdavUrl;

    @JsonProperty("samba_url")
    private String sambaUrl;

    @JsonProperty("ssh_url")
    private String sshUrl;

    @JsonProperty("ftp_url")
    private String ftpUrl;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date cancelled;

    private Boolean locked;

    @JsonProperty("root_password")
    private String rootPassword;

    @JsonProperty("backup_email")
    private String backupEmail;

    @JsonProperty("backup_status")
    private String backupStatus;

    @JsonProperty("backup_progress")
    private Integer backupProgress;

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

    @JsonProperty("sub_accounts")
    private List<StorageBoxSubAccount> subAccounts;
}
