package com.rmaslov.blog.todoTask.repository;

import com.rmaslov.blog.todoTask.model.TodoTaskDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoTaskRepository extends MongoRepository<TodoTaskDoc, ObjectId> {
}
