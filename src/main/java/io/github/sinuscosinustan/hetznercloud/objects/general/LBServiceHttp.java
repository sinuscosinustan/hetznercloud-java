package io.github.sinuscosinustan.hetznercloud.objects.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LBServiceHttp {

    @JsonProperty("cookie_name")
    private String cookieName;
    @JsonProperty("cookie_lifetime")
    private Long cookieLifetime;
    private List<Long> certificates;
    @JsonProperty("redirect_http")
    private Boolean redirectHttp;
    @JsonProperty("sticky_sessions")
    private Boolean stickySessions;
}
