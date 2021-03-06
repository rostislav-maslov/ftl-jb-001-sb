package com.rmaslov.blog.file.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "FileResponse", description = "File data(for search and list)")
public class FileResponse {
        protected String id;
        protected String title;
        protected String ownerId;
        protected String contentType;
}
