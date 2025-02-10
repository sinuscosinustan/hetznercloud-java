package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LBHealthCheck {

    private String protocol;
    private Long port;
    private Long interval;
    private Long timeout;
    private Long retries;
    private Http http;

    @Data
    public static class Http {
        private String domain;
        private String path;
        private String response;
        @JsonProperty("status_codes")
        private List<String> statusCodes;
        private Boolean tls;
    }
}
