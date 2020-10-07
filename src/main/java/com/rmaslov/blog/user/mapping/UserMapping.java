package com.rmaslov.blog.user.mapping;

import com.rmaslov.blog.user.api.response.UserResponse;
import com.rmaslov.blog.user.model.UserDoc;
import lombok.Getter;

@Getter
public class UserMapping {
    public static class ResponseMapping{
        public UserResponse convert(UserDoc userDoc){
            return UserResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }
    }

    private final ResponseMapping response = new ResponseMapping();

    public static UserMapping getInstance(){
        return new UserMapping();
    }
}
