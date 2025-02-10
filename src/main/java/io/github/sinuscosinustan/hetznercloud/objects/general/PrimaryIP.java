package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPAssigneeType;
import io.github.sinuscosinustan.hetznercloud.objects.enums.IPType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PrimaryIP {

    private Long id;
    private String ip;
    private String name;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @JsonProperty("assignee_type")
    private IPAssigneeType assigneeType;
    private IPType type;
    @JsonProperty("auto_delete")
    private Boolean autoDelete;
    private Boolean blocked;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    private Datacenter datacenter;
    @JsonProperty("dns_ptr")
    private List<DnsPTR> dnsPtr;
    private Map<String, String> labels;
}
