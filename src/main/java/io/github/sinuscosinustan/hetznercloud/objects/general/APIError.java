package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.Data;

@Data
public class APIError {

    private APIErrorCode code;
    private String message;
    private APIErrorDetails details;

}
