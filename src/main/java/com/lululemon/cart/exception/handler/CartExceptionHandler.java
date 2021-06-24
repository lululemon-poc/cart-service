package com.lululemon.cart.exception.handler;

import com.lululemon.cart.exception.*;
import com.lululemon.cart.exception.response.CartErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class CartExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        CartErrorResponse errorResponse = CartErrorResponse.builder().status(BAD_REQUEST)
                .errorCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title(error)
                .detail(ex.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());

    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        CartErrorResponse errorResponse = CartErrorResponse.builder().status(UNSUPPORTED_MEDIA_TYPE)
                .errorCode(UNSUPPORTED_MEDIA_TYPE.value())
                .timestamp(LocalDateTime.now())
                .title(builder.substring(0, builder.length() - 2))
                .detail(ex.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    /**
     * @param ex      the HttpRequestMethodNotSupportedException that is thrown when wrong http method is used
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        CartErrorResponse errorResponse = CartErrorResponse.builder().status(METHOD_NOT_ALLOWED)
                .errorCode(METHOD_NOT_ALLOWED.value())
                .timestamp(LocalDateTime.now())
                .title("Method Not allowed")
                .detail(ex.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        CartErrorResponse errorResponse = CartErrorResponse.builder().status(BAD_REQUEST)
                .errorCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title("Your request parameters didn't validate.")
                .detail(ex.getLocalizedMessage())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


    @ExceptionHandler({BadRequestException.class, InactiveCartException.class})
    protected ResponseEntity<Object> badRequestHandler(final BadRequestException ex, final WebRequest request) {
        CartErrorResponse errorResponse = CartErrorResponse.builder().status(BAD_REQUEST)
                .errorCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title(ex.getMessage())
                .detail(ex.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


    @ExceptionHandler({CartNotFoundException.class, LineItemNotFoundException.class})
    protected ResponseEntity<CartErrorResponse> cartNotFoundHandler(RuntimeException ex, WebRequest request) {
        CartErrorResponse errorResponse = CartErrorResponse.builder().status(NOT_FOUND)
                .errorCode(NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .title(ex.getMessage())
                .detail(ex.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


    @ExceptionHandler({CTServerException.class})
    protected ResponseEntity<CartErrorResponse> commercetoolsServerExceptionHandler(RuntimeException ex, WebRequest request) {
        CartErrorResponse errorResponse = CartErrorResponse.builder().status(BAD_REQUEST)
                .errorCode(BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .title(ex.getMessage())
                .detail(ex.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


}
