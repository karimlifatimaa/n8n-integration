package com.example.n8nintegration.exception;

import com.example.n8nintegration.dto.ErrorLogPayload;
import com.example.n8nintegration.service.N8nService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final N8nService n8nService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("An unexpected error occurred", ex);

        String stackTrace = getStackTraceAsString(ex);

        ErrorLogPayload payload = new ErrorLogPayload(
                "Backend",
                ex.getMessage(),
                stackTrace,
                LocalDateTime.now());

        // 1. Send to n8n
        n8nService.sendToWho(payload);

        // 2. Save to DB
        n8nService.saveLog(payload);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An internal error occurred. Administrators have been notified.");
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
