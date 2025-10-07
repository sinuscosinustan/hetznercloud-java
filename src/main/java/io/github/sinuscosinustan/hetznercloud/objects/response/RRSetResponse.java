package io.github.sinuscosinustan.hetznercloud.objects.response;

import io.github.sinuscosinustan.hetznercloud.objects.general.Action;
import io.github.sinuscosinustan.hetznercloud.objects.general.RRSet;
import lombok.Data;

import java.util.List;

@Data
public class RRSetResponse {

    private RRSet rrset;

    private List<Action> actions;
}