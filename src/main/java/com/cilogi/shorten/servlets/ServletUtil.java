// Copyright (c) 2014 Cilogi. All Rights Reserved.
//
// File:        ServletUtil.java  (27/09/14)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


class ServletUtil {
    static final Logger LOG = LoggerFactory.getLogger(ServletUtil.class);

    private ServletUtil() {}


    public static String checkURL(String url) {
         try {
             new URL(url);
             return null;
         } catch (MalformedURLException e) {
             return "Badly formed URL: " + e.getMessage();
         }
     }

    static void issue(String mimeType, int returnCode, String output, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType);
        response.setStatus(returnCode);
        response.getWriter().println(output);
    }
}
