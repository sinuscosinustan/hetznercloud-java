package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Meta {

    private Pagination pagination;

    @Data
    public static class Pagination {
        private Long page;
        @JsonProperty("per_page")
        private Long perPage;
        @JsonProperty("previous_page")
        private Long previousPage;
        @JsonProperty("next_page")
        private Long nextPage;
        @JsonProperty("last_page")
        private Long lastPage;
        @JsonProperty("total_entries")
        private Long totalEntries;
    }
}