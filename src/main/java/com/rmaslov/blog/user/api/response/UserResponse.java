package com.rmaslov.blog.user.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "UserResponse", description = "User data(for search and list)")
public class UserResponse {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
}
