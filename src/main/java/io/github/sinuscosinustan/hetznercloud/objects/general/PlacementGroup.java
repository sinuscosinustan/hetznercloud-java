package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PlacementGroup {

    private Long id;
    private String name;
    private String type;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;
    private Map<String, String> labels;
    private List<Long> servers;

}