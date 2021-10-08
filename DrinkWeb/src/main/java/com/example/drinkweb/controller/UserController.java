package com.example.drinkweb.controller;

import com.example.drinkweb.dto.GraphQLRequest;
import com.example.drinkweb.dto.order.OrderRequest;
import com.example.drinkweb.dto.order.OrderResponse;
import com.example.drinkweb.dto.perfume.PerfumeResponse;
import com.example.drinkweb.dto.review.ReviewRequest;
import com.example.drinkweb.dto.user.UserRequest;
import com.example.drinkweb.dto.user.UserResponse;
import com.example.drinkweb.exception.InputFieldException;
import com.example.drinkweb.mapper.OrderMapper;
import com.example.drinkweb.mapper.UserMapper;
import com.example.drinkweb.security.UserPrincipal;
import com.example.drinkweb.services.graphql.GraphQLProvider;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final GraphQLProvider graphQLProvider;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(userMapper.findUserByEmail(user.getEmail()));
    }

    @PostMapping("/graphql/info")
    public ResponseEntity<ExecutionResult> getUserInfoByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PutMapping("/edit")
    public ResponseEntity<UserResponse> updateUserInfo(@AuthenticationPrincipal UserPrincipal user,
                                                       @Valid @RequestBody UserRequest request,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(userMapper.updateProfile(user.getEmail(), request));
        }
    }

    @PostMapping("/cart")
    public ResponseEntity<List<PerfumeResponse>> getCart(@RequestBody List<Long> perfumesIds) {
        return ResponseEntity.ok(userMapper.getCart(perfumesIds));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(orderMapper.findOrderByEmail(user.getEmail()));
    }

    @PostMapping("/graphql/orders")
    public ResponseEntity<ExecutionResult> getUserOrdersByQuery(@RequestBody GraphQLRequest request) {
        return ResponseEntity.ok(graphQLProvider.getGraphQL().execute(request.getQuery()));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> postOrder(@Valid @RequestBody OrderRequest order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            return ResponseEntity.ok(orderMapper.postOrder(order));
        }
    }

    @PostMapping("/review")
    public ResponseEntity<PerfumeResponse> addReviewToPerfume(@Valid @RequestBody ReviewRequest review,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        } else {
            PerfumeResponse perfume = userMapper.addReviewToPerfume(review, review.getPerfumeId());
            messagingTemplate.convertAndSend("/topic/reviews/" + perfume.getId(), perfume);
            return ResponseEntity.ok(perfume);
        }
    }
}
