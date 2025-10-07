package io.github.sinuscosinustan.hetznercloud.objects.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.StorageBox;
import lombok.Data;

import java.util.List;

@Data
public class StorageBoxesResponse {

    @JsonProperty("storage_boxes")
    private List<StorageBox> storageBoxes;

    private Meta meta;
}