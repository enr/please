package com.atoito.please.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import com.google.common.base.Preconditions;

/**
 * utility class pertaining java.net.URL
 * 
 */
public class Urls {

    private static final String MALFORMED_URL_DEFAULT = "<malformed url>";

    private Urls() {
    }

    public static String decoded(URL url) {
        URL rawUrl = Preconditions.checkNotNull(url);
        String decoded = MALFORMED_URL_DEFAULT;
        try {
            decoded = URLDecoder.decode(rawUrl.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            M.debug("error decoding url " + url);
        }
        return decoded;
    }
}
