package com.masbytes.rbacapi.shared.infrastructure.handler;

//  import com.masbytes.rbacapi.permission.domain.exception.PermissionAlreadyExistsException;
//  import com.masbytes.rbacapi.permission.domain.exception.PermissionNotFoundException;
import com.masbytes.rbacapi.shared.domain.dto.ErrorResponse;
import com.masbytes.rbacapi.shared.domain.exception.DomainException;
import com.masbytes.rbacapi.shared.domain.exception.EntityAlreadyExistsException;
import com.masbytes.rbacapi.shared.domain.exception.EntityNotFoundException;
import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception handler for REST controllers. Captures domain and
 * validation exceptions, logs details, and returns standardized ErrorResponse
 * objects with appropriate HTTP status codes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles EntityNotFoundException. Logs the missing resource and returns a
     * 404 Not Found response.
     *
     * @param ex the thrown EntityNotFoundException
     * @param request the current HTTP request
     * @return ResponseEntity with ErrorResponse and HTTP 404 status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {} at path {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage(), request);
    }

    /**
     * Handles EntityAlreadyExistsException. Logs the conflict and returns a 409
     * Conflict response.
     *
     * @param ex the thrown EntityAlreadyExistsException
     * @param request the current HTTP request
     * @return ResponseEntity with ErrorResponse and HTTP 409 status
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(EntityAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("Conflict detected: {} at path {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), request);
    }

    /**
     * Handles InvalidEntityStateException. Logs the invalid state and returns a
     * 400 Bad Request response.
     *
     * @param ex the thrown InvalidEntityStateException
     * @param request the current HTTP request
     * @return ResponseEntity with ErrorResponse and HTTP 400 status
     */
    @ExceptionHandler(InvalidEntityStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidEntityStateException ex, HttpServletRequest request) {
        log.warn("Invalid state/format: {} at path {}", ex.getMessage(), request.getRequestURI());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request);
    }

    /**
     * Handles generic DomainException. Logs the violation and returns a 400 Bad
     * Request response.
     *
     * @param ex the thrown DomainException
     * @param request the current HTTP request
     * @return ResponseEntity with ErrorResponse and HTTP 400 status
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex, HttpServletRequest request) {
        log.warn("Domain logic violation: {} code: {}", ex.getMessage(), ex.getErrorCode());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), request);
    }

    /**
     * Handles validation errors from @Valid annotated requests. Aggregates
     * field error messages and returns a 400 Bad Request response.
     *
     * @param ex the thrown MethodArgumentNotValidException
     * @param request the current HTTP request
     * @return ResponseEntity with ErrorResponse and HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed for request at {}", request.getRequestURI());
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);
    }

    /**
     * Handles AccessDeniedException. Logs the forbidden access and returns a
     * 403 Forbidden response.
     * 
     * @param ex
     * @param request
     * @return 
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied at path {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to access this resource", request);
    }

    /**
     * Handles AuthenticationException. Logs the unauthorized attempt and
     * returns a 401 Unauthorized response.
     * 
     * @param ex
     * @param request
     * @return 
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        log.warn("Authentication failed at path {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication failed: " + ex.getMessage(), request);
    }

    /**
     * Handles HttpRequestMethodNotSupportedException. Logs the invalid method
     * and returns a 405 Method Not Allowed response.
     * 
     * @param ex
     * @param request
     * @return 
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("Method not supported at path {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "Request method '" + ex.getMethod() + "' not supported for this endpoint", request);
    }

    /**
     * Handles unexpected exceptions not covered by specific handlers. Logs the
     * system error and returns a 500 Internal Server Error response.
     *
     * @param ex the thrown Exception
     * @param request the current HTTP request
     * @return ResponseEntity with ErrorResponse and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("UNEXPECTED SYSTEM ERROR at path {}: ", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected error occurred", request);
    }

    /**
     * Builds a standardized ErrorResponse object and wraps it in a
     * ResponseEntity.
     *
     * @param status the HTTP status to return
     * @param errorCode the application-specific error code
     * @param message the error message
     * @param request the current HTTP request
     * @return ResponseEntity containing the ErrorResponse
     */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String errorCode, String message, HttpServletRequest request) {
        ErrorResponse errorBody = new ErrorResponse(
                status.value(), errorCode, message, request.getRequestURI(), Instant.now()
        );
        return ResponseEntity.status(status).body(errorBody);
    }
}
