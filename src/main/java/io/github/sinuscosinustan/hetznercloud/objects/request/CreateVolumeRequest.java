package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateVolumeRequest {

    private Long size;
    private String name;
    private String location;
    private boolean automount;
    private String format;
    private Long server;
    @Singular
    private Map<String, String> labels;
}
