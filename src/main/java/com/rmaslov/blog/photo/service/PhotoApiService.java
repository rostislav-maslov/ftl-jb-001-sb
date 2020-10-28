package com.rmaslov.blog.photo.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.rmaslov.blog.album.exception.AlbumNotExistException;
import com.rmaslov.blog.album.repository.AlbumRepository;
import com.rmaslov.blog.photo.api.request.PhotoSearchRequest;
import com.rmaslov.blog.photo.mapping.PhotoMapping;
import com.rmaslov.blog.base.api.request.SearchRequest;
import com.rmaslov.blog.base.api.response.SearchResponse;
import com.rmaslov.blog.photo.api.request.PhotoRequest;
import com.rmaslov.blog.photo.exception.PhotoExistException;
import com.rmaslov.blog.photo.exception.PhotoNotExistException;
import com.rmaslov.blog.photo.model.PhotoDoc;
import com.rmaslov.blog.photo.repository.PhotoRepository;
import com.rmaslov.blog.user.exception.UserNotExistException;
import com.rmaslov.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoApiService {
    private final PhotoRepository photoRepository;
    private final MongoTemplate mongoTemplate;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;

    public PhotoDoc create(MultipartFile file, ObjectId ownerId, ObjectId albumId) throws PhotoExistException, UserNotExistException, AlbumNotExistException, IOException {
        if(albumRepository.findById(albumId).isEmpty()) throw new AlbumNotExistException();
        if(userRepository.findById(ownerId).isEmpty()) throw new UserNotExistException();

        DBObject metaData = new BasicDBObject();
        metaData.put("type", file.getContentType());
        metaData.put("title", file.getOriginalFilename());

        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData
        );


        PhotoDoc photoDoc = PhotoDoc.builder()
                .id(id)
                .albumId(albumId)
                .title(file.getOriginalFilename())
                .ownerId(ownerId)
                .contentType(file.getContentType())
                .build();

        photoRepository.save(photoDoc);
        return photoDoc;
    }

    public InputStream downloadById(ObjectId id) throws ChangeSetPersister.NotFoundException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if(file == null) throw new ChangeSetPersister.NotFoundException();
        return operations.getResource(file).getInputStream();
    }

    public Optional<PhotoDoc> findById(ObjectId id){
        return  photoRepository.findById(id);
    }

    public SearchResponse<PhotoDoc> search(
             PhotoSearchRequest request
    ){
        Criteria criteria = Criteria.where("albumId").is(request.getAlbumId());

        if(request.getQuery() != null && request.getQuery()  != ""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery() , "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, PhotoDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<PhotoDoc> photoDocs = mongoTemplate.find(query, PhotoDoc.class);
        return SearchResponse.of(photoDocs, count);
    }

    public PhotoDoc update(PhotoRequest request) throws PhotoNotExistException {
        Optional<PhotoDoc> photoDocOptional = photoRepository.findById(request.getId());
        if(photoDocOptional.isPresent() == false){
            throw new PhotoNotExistException();
        }

        PhotoDoc oldDoc = photoDocOptional.get();

        PhotoDoc photoDoc = PhotoMapping.getInstance().getRequest().convert(request);
        photoDoc.setId(request.getId());
        photoDoc.setAlbumId(oldDoc.getAlbumId());
        photoDoc.setOwnerId(oldDoc.getOwnerId());
        photoDoc.setContentType(oldDoc.getContentType());
        photoRepository.save(photoDoc);

        return photoDoc;
    }

    public void delete(ObjectId id){
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
        photoRepository.deleteById(id);
    }
}
