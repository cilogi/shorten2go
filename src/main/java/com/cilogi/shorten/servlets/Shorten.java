// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        Shorten.java  (24/09/14)
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


package com.cilogi.shorten.servlets;

import com.cilogi.shorten.db.ShortURLMap;
import com.cilogi.shorten.db.ShortURLMapDAO;
import com.google.appengine.api.utils.SystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Very simple API which uses a GET to create or retrieve a short URL for a long one.
 * The output is JSON.
 * <p>Output is provided either when a valid token is supplied, or when the rate limiter is
 * happy.  If a uuid is supplied we fail if its not valid, even if the rate is OK.</p>
 */

public class Shorten extends HttpServlet {
    static final Logger LOG = LoggerFactory.getLogger(Shorten.class);

    private static final String REMOTE_HOST = "http://1-dot-shorten2go.appspot.com";
    private static final String LOCAL_HOST = "http://localhost:8080";
    private static final String URL_PARAM = "url";

    private final String host;
    private final ShortURLMapDAO shortMapDAO;

    public Shorten() {
        this.host = isDevelopmentServer() ? LOCAL_HOST : REMOTE_HOST;
        this.shortMapDAO = new ShortURLMapDAO();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter(URL_PARAM);

        String check = ServletUtil.checkURL(url);
        if (check != null) {
            ServletUtil.issue("text/plain", HttpServletResponse.SC_BAD_REQUEST, "url " + url + " badly formatted: " + check, response);
            return;
        }

        ShortURLMap map = shortMapDAO.shorten(url);
        if (map == null || map.getShortPath() == null) {
            ServletUtil.issue("text/plain", HttpServletResponse.SC_NOT_FOUND, "url " + url + " not found", response);
            return;
        }
        String shortURL =  host + "/" + map.getShortPath();
        ServletUtil.issue("text/plain", HttpServletResponse.SC_OK, shortURL, response);
    }

    private static boolean isDevelopmentServer() {
        SystemProperty.Environment.Value server = SystemProperty.environment.value();
        return server == SystemProperty.Environment.Value.Development;
    }
}

