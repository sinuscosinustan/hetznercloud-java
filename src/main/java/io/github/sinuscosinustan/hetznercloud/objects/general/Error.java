package io.github.sinuscosinustan.hetznercloud.objects.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
public class Error {

    private Code code;
    private String message;
    private Details details;

    @Getter
    @AllArgsConstructor
    public enum Code {

        FORBIDDEN("Insufficient permissions for this request"),
        INVALID_INPUT("Error while parsing or processing the input"),
        JSON_ERROR("Invalid JSON input in your request"),
        LOCKED("The item you are trying to access is locked (there is already an action running)"),
        MUST_BE_UNASSIGNED("The resource must be unassigned before being deleted"),
        NOT_FOUND("Entity not found"),
        RATE_LIMIT_EXCEEDED("Error when sending too many requests"),
        RESOURCE_LIMIT_EXCEEDED("Error when exceeding the maximum quantity of a resource for an account"),
        RESOURCE_UNAVAILABLE("The requested resource is currently unavailable"),
        SERVICE_ERROR("Error within a service"),
        UNIQUENESS_ERROR("One or more of the objects fields must be unique"),
        PROTECTED("The Action you are trying to start is protected for this resource"),
        MAINTENANCE("Cannot perform operation due to maintenance"),
        CONFLICT("The resource has changed during the request, please retry"),
        UNSUPPORTED_ERROR("The corresponding resource does not support the Action"),
        TOKEN_READONLY("The token is only allowed to perform GET requests");

        private String description;
    }

    @Data
    public static class Details {
        private static List<Field> fields;

        @Data
        public static class Field {
            private String name;
            private String[] message;
        }
    }
}
