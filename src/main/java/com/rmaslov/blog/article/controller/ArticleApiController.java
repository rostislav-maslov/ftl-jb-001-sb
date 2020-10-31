package com.rmaslov.blog.article.controller;

import com.rmaslov.blog.auth.exceptions.AuthException;
import com.rmaslov.blog.auth.exceptions.NotAccessException;
import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.article.api.request.ArticleRequest;
import com.rmaslov.blog.article.api.response.ArticleResponse;
import com.rmaslov.blog.article.exception.ArticleExistException;
import com.rmaslov.blog.article.exception.ArticleNotExistException;
import com.rmaslov.blog.article.mapping.ArticleMapping;
import com.rmaslov.blog.article.routes.ArticleApiRoutes;
import com.rmaslov.blog.article.service.ArticleApiService;
import com.rmaslov.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(value = "Article API")
public class ArticleApiController {
    private final ArticleApiService articleApiService;

    @GetMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Find article by ID", notes = "Use this when you need full info about article")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Article not found")
            }
    )
    public OkResponse<ArticleResponse> byId(
            @ApiParam(value = "Article id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(ArticleMapping.getInstance().getResponse().convert(
                articleApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @PostMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new article")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Article already exist")
    })
    public OkResponse<ArticleResponse> create(@RequestBody ArticleRequest request) throws ArticleExistException, UserNotExistException, AuthException {
        return OkResponse.of(ArticleMapping.getInstance().getResponse().convert(articleApiService.create(request)));
    }



    @GetMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Search article", notes = "Use this when you need find article by title or body")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<ArticleResponse>> search(
            @ModelAttribute SearchRequest request
    ) {
        return OkResponse.of(ArticleMapping.getInstance().getSearch().convert(
                articleApiService.search(request)
        ));
    }

    @PutMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Update article", notes = "Use this when you need update article info")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Article ID invalid")
            }
    )
    public OkResponse<ArticleResponse> updateById(
            @ApiParam(value = "Article id") @PathVariable String id,
            @RequestBody ArticleRequest articleRequest
    ) throws ArticleNotExistException, AuthException, NotAccessException {
        return OkResponse.of(ArticleMapping.getInstance().getResponse().convert(
                articleApiService.update(articleRequest)
        ));
    }

    @DeleteMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Delete article", notes = "Use this when you need delete article")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Article id") @PathVariable ObjectId id
    ) throws AuthException, NotAccessException, ChangeSetPersister.NotFoundException {
        articleApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
