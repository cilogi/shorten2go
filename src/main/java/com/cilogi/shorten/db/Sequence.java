// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        Sequence.java  (24/09/14)
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

import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * There is only one sequence entity with a given name in the database so the main
 * next method is static.
 *
 */
@Entity
@Cache
public class Sequence {
    static final Logger LOG = LoggerFactory.getLogger(Sequence.class);

    static {
        ObjectifyService.register(Sequence.class);
        ObjectifyService.register(SequenceShard.class);
    }

    /** The maximum number of shards, just in case something goes crazy and
     *  we end up with gigabytes of stupid shards
     */
    private static final int MAX_SHARDS = 10000;
    /**
     * The number of initial shards.  If we assume that each shard can cope with one
     * operation per second then we have a throughput of 50 allocations per second, or
     * about 4 million a day.  The upper limit is heading towards a billion, which I doubt
     * we'd hit on the free tier...
     */
    private static final int START_NUM_SHARDS = 50;

    private static final Random random = new Random();

    @Id
    private String name;

    @Getter @Setter
    private int numShards;

    @Getter @Setter
    private long allocationLimit;

    public static Long next(@NonNull final String sequenceName) {
        return ofy().transact(new Work<Long>() {
            public Long run() {
                Long next;

                Sequence sequence = ofy().load().key(Key.create(Sequence.class, sequenceName)).now();
                if (sequence == null) {
                    sequence = new Sequence(sequenceName);
                }

                int numShards = sequence.getNumShards();
                int shardIndex = random.nextInt(numShards);
                SequenceShard shard = getShard(sequenceName + shardIndex);
                if (shard.isExhausted()) {
                    long extra = (long)(numShards + shardIndex);
                    long currentLimit = sequence.allocationLimit;
                    long newLimit = sequence.allocationLimit + extra;
                    shard.setCurrent(currentLimit+1);
                    shard.setStop(newLimit);
                    sequence.setAllocationLimit(newLimit);
                    ofy().save().entity(sequence).now();
                    ofy().save().entity(shard).now();
                    return currentLimit;
                } else {
                    next = shard.next();
                    ofy().save().entity(shard).now();
                }
                return next;
            }
        });
    }

    public static void addShards(@NonNull final String sequenceName, final int numShards) {
        Preconditions.checkArgument(numShards > 0, "You must add at least one shard, not " + numShards);
        ofy().transact(new VoidWork() {
            public void vrun() {
                Sequence sequence = ofy().load().key(Key.create(Sequence.class, sequenceName)).now();
                if (sequence != null) {
                    sequence.addShards(numShards);
                    ofy().save().entity(sequence).now();
                }
            }
        });
    }


    private static SequenceShard getShard(@NonNull final String shardName) {
        SequenceShard shard = ofy().load().key(Key.create(SequenceShard.class, shardName)).now();
        if (shard == null) {
            shard = new SequenceShard(shardName);
        }
        return shard;
     }

    private Sequence() {
        allocationLimit = 17576;
        numShards = START_NUM_SHARDS;
    }

    private void addShards(int numShards) {
        this.numShards = Math.min(this.numShards + numShards, MAX_SHARDS);
    }

    private Sequence(@NonNull String name) {
        this();
        this.name = name;
    }


}
