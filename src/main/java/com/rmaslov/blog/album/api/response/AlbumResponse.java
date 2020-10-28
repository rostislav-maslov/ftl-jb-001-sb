package com.rmaslov.blog.album.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "AlbumResponse", description = "Album data(for search and list)")
public class AlbumResponse {
        protected String id;
        protected String title;
        protected String ownerId;
}
