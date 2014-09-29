// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        ShortURLMapDAO.java  (16/09/14)
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

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ShortURLMapDAO extends BaseDAO<ShortURLMap> {
    static final Logger LOG = LoggerFactory.getLogger(ShortURLMapDAO.class);

    private static final String DEFAULT_SEQUENCE_NAME = "shorten";

    private static final String MEMCACHE_PREFIX = "SHORT_URL_";
    private static final int NUM_RETRIES = 10;
    private static final int NUM_SHARD_INCREMENT = 50;

    static {
        ObjectifyService.register(ShortURLMap.class);
    }

    private final MemcacheService memcache;

    public ShortURLMapDAO() {
        super(ShortURLMap.class);
        memcache = MemcacheServiceFactory.getMemcacheService();
    }

    /**
     * Create the mapping for a short URL
     *
     * @param longURL  the URL to be shortened
     * @return A mapping, which has been persisted, or <code>null</code> if there is a problem
     */
    public ShortURLMap shorten(@NonNull String longURL) {
        return shorten(DEFAULT_SEQUENCE_NAME, longURL);
    }

    public ShortURLMap shorten(@NonNull String sequenceName, @NonNull String longURL) {
        ShortURLMap map = url2map(sequenceName, longURL);
        if (map == null) {
            // we need to create a mapping, store it and return the short URL
            boolean done = false;
            for (int count = 0; !done && count < NUM_RETRIES; count++) {
                Long value = Sequence.next(sequenceName);
                if (value == null) {
                    Sequence.addShards(sequenceName, NUM_SHARD_INCREMENT);
                    continue;
                }
                String shortPath = base26(value);
                map = new ShortURLMap(shortPath, longURL);
                put(map);
                memcache.put(MEMCACHE_PREFIX + sequenceName + longURL, map);
                done = true;
            }
        }
        return (map == null) ? null : map;
    }

    /**
     * Convert an integer to a string using only lower case letters.
     * This is less efficient than Base64 or Base62 but much easier to
     * type on a mobile phone
     *
     * @param number
     * @return
     */
    static String base26(long number) {
        final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
        final long LEN = (long) ALPHABET.length();
        if (number < ALPHABET.length()) {
            return String.valueOf(ALPHABET.charAt((int) number));
        } else {
            long quotient = number / LEN;
            long remainder = number % LEN;
            return base26(quotient) + String.valueOf(ALPHABET.charAt((int) remainder));
        }
    }

    /**
     * Look up the mapping for a long URL.  We need to use memcache here as Objectify
     * does not cache queries.
     *
     * @param sequenceName the name of the sequence (ie the shortening domain)
     * @param longURL      The long URL
     * @return The saved mapping, or null if there is none
     */
    private ShortURLMap url2map(@NonNull String sequenceName, String longURL) {
        ShortURLMap cached = (ShortURLMap) memcache.get(MEMCACHE_PREFIX + sequenceName + longURL);
        if (cached != null) {
            return cached;
        } else {
            cached = ofy().load().type(ShortURLMap.class).filter("longURL", longURL).first().now();
            if (cached != null) {
                memcache.put(MEMCACHE_PREFIX + sequenceName + longURL, cached);
            }
            return cached;
        }
    }
}
