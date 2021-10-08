package com.example.drinkweb.mapper;

import com.example.drinkweb.Entity.Review;
import com.example.drinkweb.Entity.User;
import com.example.drinkweb.dto.RegistrationRequest;
import com.example.drinkweb.dto.perfume.PerfumeResponse;
import com.example.drinkweb.dto.review.ReviewRequest;
import com.example.drinkweb.dto.user.UserRequest;
import com.example.drinkweb.dto.user.UserResponse;
import com.example.drinkweb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;
    private final com.example.drinkweb.mapper.PerfumeMapper perfumeMapper;
    private final UserService userService;

    private User convertToEntity(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }

    User convertToEntity(RegistrationRequest registrationRequest) {
        return modelMapper.map(registrationRequest, User.class);
    }

    private Review convertToEntity(ReviewRequest reviewRequest) {
        return modelMapper.map(reviewRequest, Review.class);
    }

    UserResponse convertToResponseDto(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse findUserById(Long userId) {
        return convertToResponseDto(userService.findUserById(userId));
    }

    public UserResponse findUserByEmail(String email) {
        return convertToResponseDto(userService.findUserByEmail(email));
    }

    public List<PerfumeResponse> getCart(List<Long> perfumesIds) {
        return perfumeMapper.convertListToResponseDto(userService.getCart(perfumesIds));
    }

    public List<UserResponse> findAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponse updateProfile(String email, UserRequest userRequest) {
        return convertToResponseDto(userService.updateProfile(email, convertToEntity(userRequest)));
    }

    public PerfumeResponse addReviewToPerfume(ReviewRequest reviewRequest, Long perfumeId) {
        return perfumeMapper.convertToResponseDto(userService.addReviewToPerfume(convertToEntity(reviewRequest), perfumeId));
    }
}
