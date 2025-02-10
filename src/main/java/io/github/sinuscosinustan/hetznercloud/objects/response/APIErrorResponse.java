package io.github.sinuscosinustan.hetznercloud.objects.response;

import lombok.Data;
import io.github.sinuscosinustan.hetznercloud.objects.general.APIError;

@Data
public class APIErrorResponse {

    private APIError error;

}
