// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        Serve.java  (23/09/14)
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Serve extends HttpServlet {
    static final Logger LOG = LoggerFactory.getLogger(Serve.class);

    private final ShortURLMapDAO dao;

    public Serve() {
        dao = new ShortURLMapDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String uri = stripLeadingSlash(request.getRequestURI()).toLowerCase();
        ShortURLMap map = dao.get(uri);
        if (map == null) {
            ServletUtil.issue("text/plain", HttpServletResponse.SC_NOT_FOUND, "No such short URL " + uri, response);
        } else {
            response.sendRedirect(map.getLongURL());
        }
    }

    protected String stripLeadingSlash(String s) {
        return (s == null) ? null : (s.startsWith("/") ? s.substring(1) : s);
    }
}
