package dev.francode.ordersystem.exceptions.advice;

import dev.francode.ordersystem.exceptions.DTOs.ApiResponseDTO;
import dev.francode.ordersystem.exceptions.DTOs.ValidationErrorDTO;
import dev.francode.ordersystem.exceptions.custom.InvalidPageableException;
import dev.francode.ordersystem.exceptions.custom.ResourceNotFoundException;
import dev.francode.ordersystem.exceptions.custom.ValidationException;
import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - URL no existe
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        String requestURL = request.getDescription(false);
        logger.error("URL no encontrada: {}", requestURL);

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "La URL solicitada no existe: " + requestURL,
                "ERR_404_URL_NOT_FOUND"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 404 - Recurso no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                "ERR_404_RESOURCE_NOT_FOUND"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 400 - Validación general
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationException(ValidationException ex) {
        logger.warn("Error de validación: {}", ex.getMessage());

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "ERR_400_VALIDATION"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 400 - Validación de campos (DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getDefaultMessage())
                .collect(Collectors.toList());

        logger.warn("Errores de validación de campos: {}", errors);

        ValidationErrorDTO response = new ValidationErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Errores de validación encontrados",
                "ERR_400_FIELD_VALIDATION"
        );
        response.setFieldErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // 400 - Formato inválido en el cuerpo de la solicitud
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn("Error de formato en el cuerpo de la solicitud: {}", ex.getMessage());

        String errorMessage = "El cuerpo de la solicitud no tiene el formato correcto.";

        // Verifica si la causa es una InvalidFormatException
        Throwable cause = ex.getCause();
        if (cause != null) {
            if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
                errorMessage = "Uno o más campos tienen un tipo de dato inválido. Verifica el formato.";
            } else if (cause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException) {
                errorMessage = "Estructura de datos incorrecta en el cuerpo de la solicitud.";
            }
        }

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                "ERR_400_INVALID_FORMAT"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // 400 - Manejo de DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.warn("Error de integridad de base de datos: {}", ex.getMessage());

        String errorMessage = "Se ha producido una violación de integridad de base de datos.";

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            String message = rootCause.getMessage();

            // Error por registro duplicado
            if (message != null && message.contains("Duplicate entry")) {
                errorMessage = "Ya existe un registro con los mismos datos. Por favor, verifica la información.";
            }

            // Error por restricción de clave foránea
            else if (message != null && message.contains("foreign key constraint fails")) {
                errorMessage = "No se puede eliminar este registro porque está relacionado con otros datos.";
            }
        }

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                "ERR_400_INTEGRITY_VIOLATION"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // 403 - Acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Acceso denegado: {}", ex.getMessage());

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "No tiene permisos para acceder a este recurso.",
                "ERR_403_ACCESS_DENIED"
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 405 - Metodo no permitido
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        logger.error("Método HTTP no soportado: {}", ex.getMethod());

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "El método HTTP " + ex.getMethod() + " no está permitido para la URL solicitada.",
                "ERR_405_METHOD_NOT_ALLOWED"
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
    @ExceptionHandler({
            PropertyReferenceException.class,
            IllegalArgumentException.class,  // ¡Asegúrate de que esta línea esté presente!
            InvalidDataAccessApiUsageException.class,
            TypeMismatchException.class
    })
    public ResponseEntity<ApiResponseDTO> handlePageableExceptions(Exception ex) {
        String errorMessage;
        String errorCode;

        if (ex instanceof PropertyReferenceException) {
            errorMessage = "Campo de ordenamiento inválido. Verifique el parámetro 'sort'.";
            errorCode = "ERR_400_INVALID_SORT";
        } else if (ex instanceof IllegalArgumentException) {
            // Captura específicamente los errores de validación de paginación
            if (ex.getMessage() != null) {
                if (ex.getMessage().contains("Page number must not be greater than")) {
                    errorMessage = "El número de página no puede ser mayor a 10000";
                } else if (ex.getMessage().contains("Page size must not be greater than")) {
                    errorMessage = "El tamaño de página no puede ser mayor a 100";
                } else if (ex.getMessage().contains("Page offset must not exceed")) {
                    errorMessage = "El número de página es demasiado grande. Máximo permitido: 10000";
                } else if (ex.getMessage().contains("Page number must not be less than zero")) {
                    errorMessage = "El número de página no puede ser negativo";
                } else if (ex.getMessage().contains("Page size must not be less than one")) {
                    errorMessage = "El tamaño de página debe ser al menos 1";
                } else {
                    errorMessage = ex.getMessage(); // Mensaje genérico para otras IllegalArgumentException
                }
            } else {
                errorMessage = "Parámetros de paginación inválidos";
            }
            errorCode = "ERR_400_INVALID_PAGINATION";
        } else if (ex instanceof InvalidDataAccessApiUsageException &&
                ex.getMessage() != null &&
                ex.getMessage().contains("Page offset exceeds Integer.MAX_VALUE")) {
            errorMessage = "El número de página es demasiado grande. Máximo permitido: 10000";
            errorCode = "ERR_400_PAGE_TOO_LARGE";
        } else if (ex instanceof TypeMismatchException) {
            errorMessage = "Los parámetros 'page' y 'size' deben ser números enteros";
            errorCode = "ERR_400_TYPE_MISMATCH";
        } else {
            errorMessage = "Error en los parámetros de la solicitud";
            errorCode = "ERR_400_BAD_REQUEST";
        }

        logger.warn("Error en parámetros de paginación: {}", ex.getMessage());

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                errorCode
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 500 - Excepciones no controladas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleUnhandledExceptions(Exception ex, WebRequest request) {
        logger.error("Error inesperado en la solicitud: {}", request.getDescription(false), ex);

        ApiResponseDTO response = new ApiResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado.",
                "ERR_500_INTERNAL_SERVER_ERROR"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
