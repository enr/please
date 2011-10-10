/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * 
 * Copyright (C) 2011 - https://github.com/enr
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

package com.atoito.please.core.components;

import java.io.File;

/**
 * Sample usage: DefaultDownloader d = new DefaultDownloader(); File downloaded
 * = d.fetch("http://path/to/file", "/destination/path");
 * 
 */
public interface Downloader {

    /**
     * Fetch the given url and returns the file with contents.
     * 
     * @param url
     * @return the file
     */
    File fetch(String url);

    /**
     * Fetch the given url, save the file in the given path and returns the file
     * with contents.
     * 
     * @param url
     * @param path
     * @return the file
     */
    File fetch(String url, String path);
}
