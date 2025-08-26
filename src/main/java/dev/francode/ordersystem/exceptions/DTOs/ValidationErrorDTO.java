package dev.francode.ordersystem.exceptions.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ValidationErrorDTO {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String errorCode;
    private List<String> fieldErrors;

    public ValidationErrorDTO(int status, String message, String errorCode) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }
}
