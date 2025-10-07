package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.StorageBox;
import lombok.Data;

import java.util.List;

@Data
public class StorageBoxResponse {

    @JsonProperty("storage_box")
    private StorageBox storageBox;

    private List<Action> actions;
}