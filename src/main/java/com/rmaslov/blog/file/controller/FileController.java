package com.rmaslov.blog.file.controller;

import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.OkResponse;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.file.api.response.FileResponse;
import com.rmaslov.blog.file.exception.FileExistException;
import com.rmaslov.blog.file.exception.FileNotExistException;
import com.rmaslov.blog.file.mapping.FileMapping;
import com.rmaslov.blog.file.routes.FileApiRoutes;
import com.rmaslov.blog.file.service.FileApiService;
import com.rmaslov.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequiredArgsConstructor
@Api(value = "File API")
public class FileController {
    private final FileApiService fileApiService;

    @PostMapping(FileApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "File already exist")
    })
    public @ResponseBody OkResponse<FileResponse> create(@RequestParam MultipartFile file, @RequestParam ObjectId ownerId) throws FileExistException, IOException, UserNotExistException {
        return OkResponse.of(FileMapping.getInstance().getResponse().convert(fileApiService.create(file, ownerId)));
    }

    @GetMapping(FileApiRoutes.DOWNLOAD)
    @ApiOperation(value = "Find file by ID", notes = "Use this when you need full info about file")
    public void byId(
            @ApiParam(value = "File id") @PathVariable ObjectId id, HttpServletResponse response
    ) throws ChangeSetPersister.NotFoundException, IOException {
        FileCopyUtils.copy(fileApiService.downloadById(id), response.getOutputStream());
    }


}
