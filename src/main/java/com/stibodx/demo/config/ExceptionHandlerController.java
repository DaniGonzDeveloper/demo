package com.stibodx.demo.config;

import com.stibodx.demo.exceptions.*;
import com.stibodx.demo.views.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("something went wrong")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionUnAuth(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JwtException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionJwt(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionNoAccess(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionNotValid(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNoFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionUserNotFound(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthNoFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionAuthNotFound(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionServiceException(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlredyExistsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExceptionUserAlredyExists(Exception e) {
        // Handle the exception and return an appropriate response
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
