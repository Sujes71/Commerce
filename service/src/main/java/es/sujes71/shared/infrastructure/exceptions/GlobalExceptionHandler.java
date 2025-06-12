package es.sujes71.shared.infrastructure.exceptions;

import es.sujes71.api.price.infrastructure.exceptions.PriceNotFoundException;
import es.sujes71.shared.domain.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "VALIDATION_ERROR",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(PriceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePriceNotFound(PriceNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "PRICE_NOT_FOUND",
        ex.getMessage(),
        HttpStatus.NOT_FOUND.value()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "INTERNAL_SERVER_ERROR",
        "An unexpected error occurred",
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}