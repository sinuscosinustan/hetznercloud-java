package io.github.sinuscosinustan.hetznercloud.objects.response;

import io.github.sinuscosinustan.hetznercloud.objects.general.Meta;
import io.github.sinuscosinustan.hetznercloud.objects.general.StorageBoxSnapshot;
import lombok.Data;

import java.util.List;

@Data
public class StorageBoxSnapshotsResponse {

    private List<StorageBoxSnapshot> snapshots;

    private Meta meta;
}