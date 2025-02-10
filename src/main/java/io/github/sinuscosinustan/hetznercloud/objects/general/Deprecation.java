package io.github.sinuscosinustan.hetznercloud.objects.general;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

@Data
public class Deprecation {

    @JsonDeserialize(using = DateDeserializer.class)
    private Date announced;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date unavailable_after;
}
