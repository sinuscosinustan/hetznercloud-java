package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.sinuscosinustan.hetznercloud.objects.general.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateRRSetRequest {

    private String name;

    private Long ttl;

    private Map<String, String> labels;

    private List<Record> records;
}