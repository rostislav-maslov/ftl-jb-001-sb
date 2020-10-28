package com.rmaslov.blog.album.controller;

import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.album.api.request.AlbumRequest;
import com.rmaslov.blog.album.api.response.AlbumResponse;
import com.rmaslov.blog.album.exception.AlbumExistException;
import com.rmaslov.blog.album.exception.AlbumNotExistException;
import com.rmaslov.blog.album.mapping.AlbumMapping;
import com.rmaslov.blog.album.routes.AlbumApiRoutes;
import com.rmaslov.blog.album.service.AlbumApiService;
import com.rmaslov.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(value = "Album API")
public class AlbumApiController {
    private final AlbumApiService albumApiService;

    @PostMapping(AlbumApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new album")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Album already exist")
    })
    public OkResponse<AlbumResponse> create(@RequestBody AlbumRequest request) throws AlbumExistException, UserNotExistException {
        return OkResponse.of(AlbumMapping.getInstance().getResponse().convert(albumApiService.create(request)));
    }

    @GetMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "Find album by ID", notes = "Use this when you need full info about album")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "Album not found")
            }
    )
    public OkResponse<AlbumResponse> byId(
            @ApiParam(value = "Album id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(AlbumMapping.getInstance().getResponse().convert(
                albumApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(AlbumApiRoutes.ROOT)
    @ApiOperation(value = "Search album", notes = "Use this when you need find album by ?????")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<AlbumResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return OkResponse.of(AlbumMapping.getInstance().getSearch().convert(
                albumApiService.search(request)
        ));
    }

    @PutMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "Update album", notes = "Use this when you need update album info")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Album ID invalid")
            }
    )
    public OkResponse<AlbumResponse> updateById(
            @ApiParam(value = "Album id")  @PathVariable String id,
            @RequestBody AlbumRequest albumRequest
            ) throws AlbumNotExistException {
        return OkResponse.of(AlbumMapping.getInstance().getResponse().convert(
                albumApiService.update(albumRequest)
        ));
    }

    @DeleteMapping(AlbumApiRoutes.BY_ID)
    @ApiOperation(value = "Delete album", notes = "Use this when you need delete album")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "Album id") @PathVariable ObjectId id
    ){
        albumApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
