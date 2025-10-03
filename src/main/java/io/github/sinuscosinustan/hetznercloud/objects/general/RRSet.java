package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.enums.RRSetType;

import java.util.List;
import java.util.Map;

@Data
public class RRSet {

    private String id;
    private String name;
    private RRSetType type;
    private Long ttl;

    private Map<String, String> labels;

    private RRSetProtection protection;

    private List<Record> records;

    private Long zone;

    @Data
    public static class RRSetProtection {
        private Boolean change;
    }
}
