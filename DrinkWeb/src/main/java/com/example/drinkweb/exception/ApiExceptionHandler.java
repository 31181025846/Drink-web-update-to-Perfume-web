package com.example.drinkweb.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(com.example.drinkweb.exception.ApiRequestException.class)
    public ResponseEntity<String> handleApiRequestException(com.example.drinkweb.exception.ApiRequestException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
    }

    @ExceptionHandler(com.example.drinkweb.exception.PasswordConfirmationException.class)
    public ResponseEntity<com.example.drinkweb.exception.PasswordConfirmationException> handlePasswordConfirmationException(com.example.drinkweb.exception.PasswordConfirmationException exception) {
        return ResponseEntity.badRequest().body(new com.example.drinkweb.exception.PasswordConfirmationException(exception.getPassword2Error()));
    }

    @ExceptionHandler(com.example.drinkweb.exception.PasswordException.class)
    public ResponseEntity<com.example.drinkweb.exception.PasswordException> handlePasswordException(com.example.drinkweb.exception.PasswordException exception) {
        return ResponseEntity.badRequest().body(new com.example.drinkweb.exception.PasswordException(exception.getPasswordError()));
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<EmailException> handleEmailError(EmailException exception) {
        return ResponseEntity.badRequest().body(new EmailException(exception.getEmailError()));
    }

    @ExceptionHandler(com.example.drinkweb.exception.CaptchaException.class)
    public ResponseEntity<com.example.drinkweb.exception.CaptchaException> handleCaptchaException(com.example.drinkweb.exception.CaptchaException exception) {
        return ResponseEntity.badRequest().body(new com.example.drinkweb.exception.CaptchaException(exception.getCaptchaError()));
    }

    @ExceptionHandler(InputFieldException.class)
    public ResponseEntity<Map<String, String>> handleInputFieldException(InputFieldException exception) {
        InputFieldException inputFieldException = new InputFieldException(exception.getBindingResult());
        return ResponseEntity.badRequest().body(inputFieldException.getErrorsMap());
    }
}
