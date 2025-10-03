package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;

@Data
public class StorageBoxSnapshot {

    private String name;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date created;

    private Long size;

    @JsonProperty("storage_box_id")
    private Long storageBoxId;
}
