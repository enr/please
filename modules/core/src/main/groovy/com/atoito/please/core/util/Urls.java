/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2012 - https://github.com/enr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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


