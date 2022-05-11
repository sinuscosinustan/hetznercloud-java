package me.tomsdevsn.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Datacenter {

    private Long id;
    private String name;
    private String description;
    private Location location;
    @JsonProperty("server_types")
    private ServerTypes serverTypes;
    private Long recommendation;

    @Data
    public static class ServerTypes {
        private List<Long> supported;
        private List<Long> available;
        @JsonProperty("available_for_migration")
        private List<Long> availableForMigration;
    }
}
