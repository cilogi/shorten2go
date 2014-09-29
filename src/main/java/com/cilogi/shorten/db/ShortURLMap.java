// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        ShortURLMap.java  (16/09/14)
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
import com.googlecode.objectify.annotation.Index;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Entity
@Cache
@EqualsAndHashCode
public class ShortURLMap implements Serializable {
    static final Logger LOG = LoggerFactory.getLogger(ShortURLMap.class);


    @Id @Getter
    private String shortPath; // the path of the short URL (a few characters)

    @Index @Getter
    private String longURL; // the long URL, the one which is shortened and to which we redirect


    private ShortURLMap() {}

    /**
     * Create a new map
     * @param shortPath  The short path, which is a few characters.  The hostname and so on is
     *                   not needed, and indeed may vary.
     * @param longURL  The URL to which the redirect URL points.
     */
    public ShortURLMap(@NonNull String shortPath, @NonNull String longURL) {
        this.shortPath = shortPath;
        this.longURL = longURL;
    }
}
