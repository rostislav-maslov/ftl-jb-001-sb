package com.rmaslov.blog.user.api.response;

import com.rmaslov.blog.user.model.Address;
import com.rmaslov.blog.user.model.Company;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ApiModel(value = "UserFullResponse", description = "User full data")
public class UserFullResponse extends UserResponse {
    private Address address;
    private Company company;
}
