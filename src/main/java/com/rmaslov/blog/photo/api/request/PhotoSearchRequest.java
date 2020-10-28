package com.rmaslov.blog.photo.api.request;

import com.rmaslov.blog.base.api.request.SearchRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoSearchRequest extends SearchRequest {
    private ObjectId albumId;
}
