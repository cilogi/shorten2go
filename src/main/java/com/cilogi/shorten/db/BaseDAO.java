// Copyright (c) 2013 Cilogi. All Rights Reserved.
//
// File:        BaseDAO.java  (26/02/13)
// Author:      tim
//
// Copyright in the whole and every part of this source file belongs to
// Cilogi (the Author) and may not be used, sold, licenced, 
// transferred, copied or reproduced in whole or in part in 
// any manner or form or in or on any media to any person other than 
// in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.shorten.db;

import com.googlecode.objectify.Key;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class BaseDAO<T> {
    static final Logger LOG = LoggerFactory.getLogger(BaseDAO.class);

    private final Class clazz;

    protected BaseDAO(Class clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings({"unchecked"})
    public T get(@NonNull String id) {
        T db = (T) ofy().load().key(Key.create(clazz, id)).now();
        return db;
    }

    @SuppressWarnings({"unchecked"})
    public T get(long id) {
        T db = (T) ofy().load().key(Key.create(clazz, id)).now();
        return db;
    }

    public void put(@NonNull T object) {
        ofy().save().entity(object).now();
    }

    @SuppressWarnings({"unchecked"})
    public void delete(@NonNull String id) {
        ofy().delete().key(Key.create(clazz, id));
    }

}
