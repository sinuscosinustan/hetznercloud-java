package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStorageBoxSubAccountRequest {

    @NonNull
    private String username;
    @NonNull
    private String password;

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
}