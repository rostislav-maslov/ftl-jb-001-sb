package com.rmaslov.blog.todoTask.mapping;

import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.base.mapping.BaseMapping;
import com.rmaslov.blog.todoTask.api.request.TodoTaskRequest;
import com.rmaslov.blog.todoTask.api.response.TodoTaskResponse;
import com.rmaslov.blog.todoTask.model.TodoTaskDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TodoTaskMapping {
    public static class RequestMapping {

        public TodoTaskDoc convert(TodoTaskRequest todoTaskRequest, ObjectId ownerId) {
            return TodoTaskDoc.builder()
                    .id(todoTaskRequest.getId())
                    .title(todoTaskRequest.getTitle())
                    .ownerId(ownerId)
                    .completed(todoTaskRequest.getCompleted())
                    .files(todoTaskRequest.getFiles())
                    .build();
        }

    }


    public static class ResponseMapping extends BaseMapping<TodoTaskDoc, TodoTaskResponse> {

        @Override
        public TodoTaskResponse convert(TodoTaskDoc todoTaskDoc) {
            return TodoTaskResponse.builder()
                    .id(todoTaskDoc.getId().toString())
                    .title(todoTaskDoc.getTitle())
                    .ownerId(todoTaskDoc.getOwnerId().toString())
                    .completed(todoTaskDoc.getCompleted())
                    .files(todoTaskDoc.getFiles().stream().map(ObjectId::toString).collect(Collectors.toList()))
                    .build();
        }

        @Override
        public TodoTaskDoc unmapping(TodoTaskResponse todoTaskResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<TodoTaskDoc>, SearchResponse<TodoTaskResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<TodoTaskResponse> convert(SearchResponse<TodoTaskDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );
        }

        @Override
        public SearchResponse<TodoTaskDoc> unmapping(SearchResponse<TodoTaskResponse> todoTaskResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static TodoTaskMapping getInstance() {
        return new TodoTaskMapping();
    }
}
