package com.rmaslov.blog.user.service;

import com.rmaslov.blog.user.api.request.RegistrationRequest;
import com.rmaslov.blog.user.api.request.UserRequest;
import com.rmaslov.blog.user.exception.UserExistException;
import com.rmaslov.blog.user.exception.UserNotExistException;
import com.rmaslov.blog.user.model.UserDoc;
import com.rmaslov.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserDoc registration(RegistrationRequest request) throws UserExistException {
        if (userRepository.findByEmail(request.getEmail()).isPresent() == true) {
            throw new UserExistException();
        }

        UserDoc userDoc = new UserDoc();
        userDoc.setEmail(request.getEmail());
        userDoc.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        userDoc = userRepository.save(userDoc);
        return userDoc;
    }

    public Optional<UserDoc> findById(ObjectId id){
        return  userRepository.findById(id);
    }

    public List<UserDoc> search(
             String queryString,
             Integer size,
             Long skip
    ){
        Criteria criteria = new Criteria();
        if(queryString != null && queryString != ""){
            criteria = criteria.orOperator(
                    Criteria.where("firstName").regex(queryString, "i"),
                    Criteria.where("lastName").regex(queryString, "i"),
                    Criteria.where("email").regex(queryString, "i")
            );
        }

        Query query = new Query(criteria);
        query.limit(size);
        query.skip(skip);

        return mongoTemplate.find(query, UserDoc.class);
    }

    public UserDoc update(UserRequest request) throws UserNotExistException {
        Optional<UserDoc> userDocOptional = userRepository.findById(request.getId());
        if(userDocOptional.isPresent() == false){
            throw new UserNotExistException();
        }

        UserDoc userDoc = userDocOptional.get();
        userDoc.setLastName(request.getLastName());
        userDoc.setFirstName(request.getFirstName());
        userDoc.setAddress(request.getAddress());
        userDoc.setCompany(request.getCompany());
        userRepository.save(userDoc);

        return userDoc;
    }

    public void delete(ObjectId id){
        userRepository.deleteById(id);
    }
}
