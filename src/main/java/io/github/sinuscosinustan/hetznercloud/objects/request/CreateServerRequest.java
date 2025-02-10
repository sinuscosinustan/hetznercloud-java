package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateServerRequest {

    private String name;

    @JsonProperty("server_type")
    private String serverType;
    private String datacenter;
    private String location;
    private String image;

    @JsonProperty("start_after_create")
    private boolean startAfterCreate;

    @Singular
    private Map<String, String> labels;

    @JsonProperty("ssh_keys")
    @Singular
    private List<Object> sshKeys;

    @JsonProperty("user_data")
    private String userData;

    @Singular
    private List<Long> volumes;
    private boolean automount;

    @Singular
    private List<Long> networks;

    @JsonProperty("placement_group")
    private Long placementGroup;

    @JsonProperty("firewalls")
    private List<CreateServerRequestFirewall> firewalls;

    @JsonProperty("public_net")
    private ServerPublicNetRequest publicNet;
}
