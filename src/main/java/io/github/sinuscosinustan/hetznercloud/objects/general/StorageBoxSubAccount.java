package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;

@Data
public class StorageBoxSubAccount {

    private String username;
    private String password;
    private String name;

    @JsonProperty("home_path")
    private String homePath;

    @JsonProperty("readonly_access")
    private Boolean readonlyAccess;

    @JsonProperty("writeable_access")
    private Boolean writeableAccess;

    @JsonProperty("ssh_support")
    private Boolean sshSupport;

    @JsonProperty("webdav_support")
    private Boolean webdavSupport;

    @JsonProperty("samba_support")
    private Boolean sambaSupport;

    @JsonProperty("ftp_support")
    private Boolean ftpSupport;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
}
