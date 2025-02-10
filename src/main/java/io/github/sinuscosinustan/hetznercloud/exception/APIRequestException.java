package io.github.sinuscosinustan.hetznercloud.exception;

import lombok.Getter;
import io.github.sinuscosinustan.hetznercloud.objects.response.APIErrorResponse;

public class APIRequestException extends RuntimeException {

    private static final long serialVersionUID = -5504832422225211786L;
    private static final String DEFAULT_EXCEPTION_MSG = "Encountered an Error while calling the Hetzner-API: [%s] %s";

    @Getter
    private final APIErrorResponse apiErrorResponse;

    public APIRequestException(APIErrorResponse apiErrorResponse) {
        this(String.format(DEFAULT_EXCEPTION_MSG,
                apiErrorResponse.getError().getCode(),
                apiErrorResponse.getError().getMessage()), null, apiErrorResponse);
    }

    public APIRequestException(String message, APIErrorResponse apiErrorResponse) {
        this(message, null, apiErrorResponse);
    }

    public APIRequestException(Throwable cause, APIErrorResponse apiErrorResponse) {
        this(cause != null ? cause.getMessage() : null, cause, apiErrorResponse);
    }

    public APIRequestException(String message, Throwable cause, APIErrorResponse apiErrorResponse) {
        super(message);
        if (cause != null) super.initCause(cause);

        this.apiErrorResponse = apiErrorResponse;
    }

}
