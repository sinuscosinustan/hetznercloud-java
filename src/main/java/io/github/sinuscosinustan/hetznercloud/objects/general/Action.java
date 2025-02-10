package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.deserialize.DateDeserializer;

import java.util.Date;
import java.util.List;

@Data
public class Action {

    private Long id;
    private String command;
    private String status;
    private Long progress;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date started;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date finished;
    private List<Resources> resources;
    private Error error;

    @Data
    public static class Resources {
        private Long id;
        private String type;
    }

    @Data
    public static class Error {
        private String code;
        private String message;
    }
}
