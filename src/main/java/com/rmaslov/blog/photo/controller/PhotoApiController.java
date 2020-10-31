package com.rmaslov.blog.photo.controller;

import com.rmaslov.blog.album.exception.AlbumNotExistException;
import com.rmaslov.blog.auth.exceptions.AuthException;
import com.rmaslov.blog.auth.exceptions.NotAccessException;
import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.photo.api.request.PhotoRequest;
import com.rmaslov.blog.photo.api.request.PhotoSearchRequest;
import com.rmaslov.blog.photo.api.response.PhotoResponse;
import com.rmaslov.blog.photo.exception.PhotoExistException;
import com.rmaslov.blog.photo.exception.PhotoNotExistException;
import com.rmaslov.blog.photo.mapping.PhotoMapping;
import com.rmaslov.blog.photo.routes.PhotoApiRoutes;
import com.rmaslov.blog.photo.service.PhotoApiService;
import com.rmaslov.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(value = "Photo API")
public class PhotoApiController {
    private final PhotoApiService photoApiService;



    @GetMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Find photo by ID", notes = "Use this when you need full info about photo")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Photo not found")
            }
    )
    public OkResponse<PhotoResponse> byId(
            @ApiParam(value = "Photo id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(PhotoMapping.getInstance().getResponse().convert(
                photoApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Search photo", notes = "Use this when you need find photo by ?????")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<PhotoResponse>> search(
            @ModelAttribute PhotoSearchRequest request
            ){
        return OkResponse.of(PhotoMapping.getInstance().getSearch().convert(
                photoApiService.search(request)
        ));
    }

    @PutMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Update photo", notes = "Use this when you need update photo info")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Photo ID invalid")
            }
    )
    public OkResponse<PhotoResponse> updateById(
            @ApiParam(value = "Photo id")  @PathVariable String id,
            @RequestBody PhotoRequest photoRequest
            ) throws PhotoNotExistException, AuthException, NotAccessException {
        return OkResponse.of(PhotoMapping.getInstance().getResponse().convert(
                photoApiService.update(photoRequest)
        ));
    }

    @DeleteMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Delete photo", notes = "Use this when you need delete photo")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Photo id") @PathVariable ObjectId id
    ) throws AuthException, NotAccessException, ChangeSetPersister.NotFoundException {
        photoApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
