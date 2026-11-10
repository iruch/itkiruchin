package com.itacademy.iruchin.test.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path)
    {
    public static ApiError of(HttpStatusCode status, String message, String path) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        String error = httpStatus != null ? httpStatus.getReasonPhrase() : "Error";
        return new ApiError(Instant.now(), status.value(), error, message, path);
    }
}

