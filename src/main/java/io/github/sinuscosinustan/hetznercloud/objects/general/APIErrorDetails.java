package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;

import java.util.List;

@Data
public class APIErrorDetails {

    private List<Field> fields;

    @Data
    private static class Field {
        private String name;
        private String[] messages;
    }

}
