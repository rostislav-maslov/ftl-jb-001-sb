package com.rmaslov.blog.base.service;

import com.rmaslov.blog.auth.exceptions.AuthException;
import com.rmaslov.blog.auth.exceptions.NotAccessException;
import com.rmaslov.blog.auth.service.AuthService;
import com.rmaslov.blog.user.model.UserDoc;
import org.bson.types.ObjectId;

public abstract class CheckAccess<T> {


    protected abstract ObjectId getOwnerFromEntity(T entity);

    protected UserDoc checkAccess(T entity) throws AuthException, NotAccessException {
        ObjectId ownerId = getOwnerFromEntity(entity);

        UserDoc owner = authService().currentUser();

        if(owner.getId().equals(ownerId) == false) throw new NotAccessException();

        return owner;
    }

    protected abstract AuthService authService();
}
