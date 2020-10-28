package com.rmaslov.blog.todoTask.api.request;

import com.rmaslov.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TodoTaskSearchRequest extends SearchRequest {
    @ApiParam(name = "OwnerId", value = "Search by User", required = true)
    private ObjectId ownerId;
}
