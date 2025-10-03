package io.github.sinuscosinustan.hetznercloud.objects.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.sinuscosinustan.hetznercloud.objects.enums.RRSetType;
import io.github.sinuscosinustan.hetznercloud.objects.general.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateRRSetRequest {

    @NonNull
    private String name;

    @NonNull
    private RRSetType type;

    private Long ttl;

    private Map<String, String> labels;

    @NonNull
    private List<Record> records;
}