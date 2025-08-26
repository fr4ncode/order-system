package dev.francode.ordersystem.exceptions.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String errorCode;

    public ApiResponseDTO(int status, String message, String errorCode) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}