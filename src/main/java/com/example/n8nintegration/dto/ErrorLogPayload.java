package com.example.n8nintegration.dto;

import java.time.LocalDateTime;

public record ErrorLogPayload(
        String service_name,
        String error_message,
        String error_stacktrace,
        LocalDateTime timestamp) {
}
