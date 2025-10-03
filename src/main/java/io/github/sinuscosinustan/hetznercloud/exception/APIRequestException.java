package io.github.sinuscosinustan.hetznercloud.exception;

import lombok.Getter;
import io.github.sinuscosinustan.hetznercloud.objects.response.APIErrorResponse;

public class APIRequestException extends RuntimeException {

    private static final long serialVersionUID = -5504832422225211786L;
    private static final String DEFAULT_EXCEPTION_MSG = "Encountered an Error while calling the Hetzner-API: [%s] %s";

    @Getter
    private final APIErrorResponse apiErrorResponse;

    @Getter
    private final String correlationId;

    public APIRequestException(APIErrorResponse apiErrorResponse) {
        this(apiErrorResponse, null);
    }

    public APIRequestException(APIErrorResponse apiErrorResponse, String correlationId) {
        this(String.format(DEFAULT_EXCEPTION_MSG,
                apiErrorResponse.getError().getCode(),
                apiErrorResponse.getError().getMessage()), null, apiErrorResponse, correlationId);
    }

    public APIRequestException(String message, APIErrorResponse apiErrorResponse) {
        this(message, apiErrorResponse, null);
    }

    public APIRequestException(String message, APIErrorResponse apiErrorResponse, String correlationId) {
        this(message, null, apiErrorResponse, correlationId);
    }

    public APIRequestException(Throwable cause, APIErrorResponse apiErrorResponse) {
        this(cause, apiErrorResponse, null);
    }

    public APIRequestException(Throwable cause, APIErrorResponse apiErrorResponse, String correlationId) {
        this(cause != null ? cause.getMessage() : null, cause, apiErrorResponse, correlationId);
    }

    public APIRequestException(String message, Throwable cause, APIErrorResponse apiErrorResponse, String correlationId) {
        super(message);
        if (cause != null) super.initCause(cause);

        this.apiErrorResponse = apiErrorResponse;
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        String baseMessage = super.toString();
        if (correlationId != null) {
            return baseMessage + " (Correlation ID: " + correlationId + ")";
        }
        return baseMessage;
    }

}
