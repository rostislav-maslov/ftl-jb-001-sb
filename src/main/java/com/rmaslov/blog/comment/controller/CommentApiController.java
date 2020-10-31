package com.rmaslov.blog.comment.controller;

import com.rmaslov.blog.article.exception.ArticleNotExistException;
import com.rmaslov.blog.auth.exceptions.AuthException;
import com.rmaslov.blog.auth.exceptions.NotAccessException;
import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.comment.api.request.CommentRequest;
import com.rmaslov.blog.comment.api.request.CommentSearchRequest;
import com.rmaslov.blog.comment.api.response.CommentResponse;
import com.rmaslov.blog.comment.exception.CommentExistException;
import com.rmaslov.blog.comment.exception.CommentNotExistException;
import com.rmaslov.blog.comment.mapping.CommentMapping;
import com.rmaslov.blog.comment.routes.CommentApiRoutes;
import com.rmaslov.blog.comment.service.CommentApiService;
import com.rmaslov.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(value = "Comment API")
public class CommentApiController {
    private final CommentApiService commentApiService;

    @PostMapping(CommentApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Comment already exist")
    })
    public OkResponse<CommentResponse> create(@RequestBody CommentRequest request) throws  ArticleNotExistException, AuthException {
        return OkResponse.of(CommentMapping.getInstance().getResponse().convert(commentApiService.create(request)));
    }

    @GetMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "Find comment by ID", notes = "Use this when you need full info about comment")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Comment not found")
            }
    )
    public OkResponse<CommentResponse> byId(
            @ApiParam(value = "Comment id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(CommentMapping.getInstance().getResponse().convert(
                commentApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(CommentApiRoutes.ROOT)
    @ApiOperation(value = "Search comment", notes = "Use this when you need find comment by message Or Article or User")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<CommentResponse>> search(
            @ModelAttribute CommentSearchRequest request
    ) {
        return OkResponse.of(CommentMapping.getInstance().getSearch().convert(
                commentApiService.search(request)
        ));
    }

    @PutMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "Update comment", notes = "Use this when you need update comment info")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Comment ID invalid")
            }
    )
    public OkResponse<CommentResponse> updateById(
            @ApiParam(value = "Comment id") @PathVariable String id,
            @RequestBody CommentRequest commentRequest
    ) throws CommentNotExistException, AuthException, NotAccessException {
        return OkResponse.of(CommentMapping.getInstance().getResponse().convert(
                commentApiService.update(commentRequest)
        ));
    }

    @DeleteMapping(CommentApiRoutes.BY_ID)
    @ApiOperation(value = "Delete comment", notes = "Use this when you need delete comment")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Comment id") @PathVariable ObjectId id
    ) throws AuthException, NotAccessException, ChangeSetPersister.NotFoundException {
        commentApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
