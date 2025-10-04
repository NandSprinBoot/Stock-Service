package com.stock.exception;

import com.stock.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(NoStockFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleResourceNotFound(NoStockFound ex, ServerHttpRequest request) {
        return Mono.just(
                ErrorResponse.of(
                        "NOT_FOUND",
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getPath().value()
                )
        );
    }

}
