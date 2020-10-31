package com.rmaslov.blog.auth.controller;

import com.rmaslov.blog.auth.api.request.AuthRequest;
import com.rmaslov.blog.auth.api.response.AuthResponse;
import com.rmaslov.blog.auth.exceptions.AuthException;
import com.rmaslov.blog.auth.routes.AuthRoutes;
import com.rmaslov.blog.auth.service.AuthService;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.user.api.request.RegistrationRequest;
import com.rmaslov.blog.user.api.response.UserFullResponse;
import com.rmaslov.blog.user.exception.UserExistException;
import com.rmaslov.blog.user.exception.UserNotExistException;
import com.rmaslov.blog.user.mapping.UserMapping;
import com.rmaslov.blog.user.service.UserApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserApiService userApiService;
    private final AuthService authService;

    @PostMapping(AuthRoutes.REGISTRATION)
    @ApiOperation(value = "Register", notes = "Use this when you need register and create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User already exist")
    })
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFull().convert(userApiService.registration(request)));
    }

    @PostMapping(AuthRoutes.AUTH)
    @ApiOperation(value = "Auth", notes = "Get token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User not exist"),
            @ApiResponse(code = 401, message = "Bad password")
    })
    public OkResponse<AuthResponse> auth(@RequestBody AuthRequest authRequest) throws AuthException, UserNotExistException {
        return OkResponse.of(authService.auth(authRequest));
    }
}
