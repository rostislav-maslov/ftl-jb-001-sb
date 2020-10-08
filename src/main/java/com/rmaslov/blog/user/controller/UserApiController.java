package com.rmaslov.blog.user.controller;

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
    public UserFullResponse registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return UserMapping.getInstance().getResponseFull().convert(userApiService.registration(request));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public UserFullResponse byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return UserMapping.getInstance().getResponseFull().convert(
                userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        );
    }

    @GetMapping(UserApiRoutes.ROOT)
    public List<UserResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "1") Integer size,
            @RequestParam(required = false, defaultValue = "0") Long skip
    ){
        return UserMapping.getInstance().getSearch().convert(
                userApiService.search(query, size, skip)
        );
    }

    @PutMapping(UserApiRoutes.BY_ID)
    public UserFullResponse updateById(
            @PathVariable String id,
            @RequestBody UserRequest userRequest
            ) throws UserNotExistException {
        return UserMapping.getInstance().getResponseFull().convert(
                userApiService.update(userRequest)
        );
    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    public String deleteById(@PathVariable ObjectId id){
        userApiService.delete(id);
        return HttpStatus.OK.toString();
    }
}
