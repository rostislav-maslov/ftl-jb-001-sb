package com.rmaslov.blog.user.controller;

import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.user.api.request.RegistrationRequest;
import com.rmaslov.blog.user.api.request.UserRequest;
import com.rmaslov.blog.user.api.response.UserFullResponse;
import com.rmaslov.blog.user.api.response.UserResponse;
import com.rmaslov.blog.user.exception.UserExistException;
import com.rmaslov.blog.user.exception.UserNotExistException;
import com.rmaslov.blog.user.mapping.UserMapping;
import com.rmaslov.blog.user.routes.UserApiRoutes;
import com.rmaslov.blog.user.service.UserApiService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFull().convert(userApiService.registration(request)));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public OkResponse<UserFullResponse> byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(UserMapping.getInstance().getResponseFull().convert(
                userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(UserApiRoutes.ROOT)
    public OkResponse<SearchResponse<UserResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return OkResponse.of(UserMapping.getInstance().getSearch().convert(
                userApiService.search(request)
        ));
    }

    @PutMapping(UserApiRoutes.BY_ID)
    public OkResponse<UserFullResponse> updateById(
            @PathVariable String id,
            @RequestBody UserRequest userRequest
            ) throws UserNotExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFull().convert(
                userApiService.update(userRequest)
        ));
    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    public OkResponse<String> deleteById(@PathVariable ObjectId id){
        userApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
