package com.grammr.config.web;

import com.grammr.common.ErrorResponse;
import com.grammr.domain.exception.BadRequestException;
import com.grammr.domain.exception.ConfigurationNotAvailableException;
import com.grammr.domain.exception.InflectionNotAvailableException;
import com.grammr.domain.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public final ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.info(ex.getMessage());
    return new ResponseEntity<>(ErrorResponse.withMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<ErrorResponse> handleConfigurationNotAvailable(ConfigurationNotAvailableException ex) {
    log.info(ex.getMessage());
    return new ResponseEntity<>(ErrorResponse.withMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InflectionNotAvailableException.class)
  public final ResponseEntity<ErrorResponse> handleInflectionNotAvailable(InflectionNotAvailableException ex) {
    return new ResponseEntity<>(ErrorResponse.withMessage(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    log.info("Generic exception raised when processing request", ex);
    return new ResponseEntity<>(ErrorResponse.withMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodNotAllowedException.class)
  public final ResponseEntity<ErrorResponse> handleMethodNotAllowed(Exception ex) {
    return new ResponseEntity<>(ErrorResponse.withMessage(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorResponse> handleInvalidBody(Exception ex) {
    log.warn("Invalid message body in request: {}", ex.getMessage());
    return new ResponseEntity<>(ErrorResponse.withMessage("Invalid message body"), HttpStatus.BAD_REQUEST);
  }
}
