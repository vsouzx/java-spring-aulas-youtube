package br.com.souza.eda_order_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    private ResponseEntity<Object> handleGeneralError(Exception e, WebRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(e.getMessage());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.INTERNAL_SERVER_ERROR  , request);
    }

    @ExceptionHandler({NotFoundException.class})
    private ResponseEntity<Object> handleNotFoundException(Exception e, WebRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(e.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({InsufficientStockException.class})
    private ResponseEntity<Object> handleInsufficientStockException(Exception e, WebRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(e.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST  , request);
    }
}
