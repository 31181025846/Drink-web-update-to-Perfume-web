package com.example.drinkweb.controller;

import com.example.drinkweb.dto.RegistrationRequest;
import com.example.drinkweb.exception.ApiRequestException;
import com.example.drinkweb.exception.EmailException;
import com.example.drinkweb.exception.InputFieldException;
import com.example.drinkweb.exception.PasswordException;
import com.example.drinkweb.mapper.AuthenticationMapper;
import com.example.drinkweb.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final AuthenticationMapper authenticationMapper;
    private final ControllerUtils controllerUtils;

    @PostMapping
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationRequest user, BindingResult bindingResult) {
        controllerUtils.captchaValidation(user.getCaptcha());

        if (controllerUtils.isPasswordDifferent(user.getPassword(), user.getPassword2())) {
            throw new PasswordException("Passwords do not match.");
        }
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        if (!authenticationMapper.registerUser(user)) {
            throw new EmailException("Email is already used.");
        }
        return ResponseEntity.ok("User successfully registered.");
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activateEmailCode(@PathVariable String code) {
        if (!authenticationMapper.activateUser(code)) {
            throw new ApiRequestException("Activation code not found.", HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok("User successfully activated.");
        }
    }
}
