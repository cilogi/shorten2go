// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        SequenceShard.java  (24/09/14)
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


import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Cache
class SequenceShard {
    static final Logger LOG = LoggerFactory.getLogger(SequenceShard.class);

    /**
     * The name is of the form <code>sequenceName + index</code> where sequenceName is the
     * name of the sequence and index is the index of the shard.
     */
    @Id
    private String index;

    /**
     * The next number to issue
     */
    @Setter
    private long current;

    /**
     * The last (exclusive, so it won't be issued) number to be issued.
     */
    @Setter
    private long stop;

    private SequenceShard() {}

    SequenceShard(@NonNull String index) {
        this.index = index;
    }

    /**
     * Get the next number.
     * @return The next number. Note that the limit isn't checked, that's done by <code>isExhausted</code>.
     */
    long next() {
        long response = current;
        current++;
        return response;
    }

    boolean isExhausted() {
        return current >= stop;
    }
}
