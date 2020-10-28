package com.rmaslov.blog.photo.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value = "PhotoRequest", description = "Model for update photo")
public class PhotoRequest {
        private ObjectId id;
        private String title;
        private ObjectId ownerId;
        private ObjectId albumId;
        private String contentType;
}
