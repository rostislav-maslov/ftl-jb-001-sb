package com.rmaslov.blog.article.service;

import com.rmaslov.blog.article.mapping.ArticleMapping;
import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.article.api.request.ArticleRequest;
import com.rmaslov.blog.article.exception.ArticleExistException;
import com.rmaslov.blog.article.exception.ArticleNotExistException;
import com.rmaslov.blog.article.model.ArticleDoc;
import com.rmaslov.blog.article.repository.ArticleRepository;
import com.rmaslov.blog.user.exception.UserNotExistException;
import com.rmaslov.blog.user.model.UserDoc;
import com.rmaslov.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleApiService {
    private final ArticleRepository articleRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;

    public ArticleDoc create(ArticleRequest request) throws ArticleExistException, UserNotExistException {
        Optional<UserDoc> userDoc = userRepository.findById(request.getOwnerId());
        if(userDoc.isPresent() == false) throw new UserNotExistException();

        ArticleDoc articleDoc = ArticleMapping.getInstance().getRequest().convert(request);
        articleRepository.save(articleDoc);
        return articleDoc;
    }

    public Optional<ArticleDoc> findById(ObjectId id){
        return  articleRepository.findById(id);
    }

    public SearchResponse<ArticleDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()  != ""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery() , "i"),
                    Criteria.where("body").regex(request.getQuery() , "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, ArticleDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<ArticleDoc> articleDocs = mongoTemplate.find(query, ArticleDoc.class);
        return SearchResponse.of(articleDocs, count);
    }

    public ArticleDoc update(ArticleRequest request) throws ArticleNotExistException {
        Optional<ArticleDoc> articleDocOptional = articleRepository.findById(request.getId());
        if(articleDocOptional.isPresent() == false){
            throw new ArticleNotExistException();
        }

        ArticleDoc articleDoc = ArticleMapping.getInstance().getRequest().convert(request);
        articleDoc.setId(request.getId());
        articleRepository.save(articleDoc);

        return articleDoc;
    }

    public void delete(ObjectId id){
        articleRepository.deleteById(id);
    }
}
