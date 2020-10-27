package com.rmaslov.blog.article.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "ArticleResponse", description = "Article data(for search and list)")
public class ArticleResponse {
        protected String id;
        protected String title;
        protected String body;
        protected String ownerId;
}
