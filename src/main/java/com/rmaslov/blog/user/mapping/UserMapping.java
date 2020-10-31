package com.rmaslov.blog.user.mapping;

import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.base.mapping.BaseMapping;
import com.rmaslov.blog.user.api.request.UserRequest;
import com.rmaslov.blog.user.api.response.UserFullResponse;
import com.rmaslov.blog.user.api.response.UserResponse;
import com.rmaslov.blog.user.model.UserDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class UserMapping {
    public static class RequestMapping{

        public UserDoc convert(UserRequest userRequest, ObjectId userId){
            return UserDoc.builder()
                    .id(userId)
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .build();
        }

    }

    public static class ResponseMapping extends BaseMapping<UserDoc, UserResponse> {

        @Override
        public UserResponse convert(UserDoc userDoc){
            return UserResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }

        @Override
        public UserDoc unmapping(UserResponse userResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class ResponseFullMapping extends BaseMapping<UserDoc, UserFullResponse> {

        @Override
        public UserFullResponse convert(UserDoc userDoc){
            return UserFullResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .address(userDoc.getAddress())
                    .company(userDoc.getCompany())
                    .build();
        }

        @Override
        public UserDoc unmapping(UserFullResponse response) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<UserDoc>, SearchResponse<UserResponse>>{
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<UserResponse> convert(SearchResponse<UserDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<UserDoc> unmapping(SearchResponse<UserResponse> userResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final ResponseFullMapping responseFull = new ResponseFullMapping();
    private final SearchMapping search = new SearchMapping();

    public static UserMapping getInstance(){
        return new UserMapping();
    }
}
