package com.example.drinkweb.mapper;

import com.example.drinkweb.dto.RegistrationRequest;
import com.example.drinkweb.dto.auth.AuthenticationResponse;
import com.example.drinkweb.dto.user.UserResponse;
import com.example.drinkweb.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public UserResponse findByPasswordResetCode(String code) {
        return userMapper.convertToResponseDto(authenticationService.findByPasswordResetCode(code));
    }

    public AuthenticationResponse login(String email) {
        Map<String, String> resultMap = authenticationService.login(email);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setEmail(resultMap.get("email"));
        response.setToken(resultMap.get("token"));
        response.setUserRole(resultMap.get("userRole"));
        return response;
    }

    public boolean registerUser(RegistrationRequest registrationRequest) {
        return authenticationService.registerUser(userMapper.convertToEntity(registrationRequest));
    }

    public boolean activateUser(String code) {
        return authenticationService.activateUser(code);
    }

    public boolean sendPasswordResetCode(String email) {
        return authenticationService.sendPasswordResetCode(email);
    }

    public String passwordReset(String email, String password) {
        return authenticationService.passwordReset(email, password);
    }
}
