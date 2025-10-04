package com.stock.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    // default value for timestamp
    public static ErrorResponse of(String errorCode, String errorMessage, int status, String path) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .status(status)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}