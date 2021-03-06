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

package com.atoito.please.core.util;

/**
 * Utility class pertaining reporters.
 * 
 */
public class Reporters {

    private static Reporter current;

    private Reporters() {
    }

    public static Reporter defaultReporter() {
        if (current != null) {
            current.info("you are asking to create a reporter, but a reporter is already active...");
        } else {
            current = new DefaultReporter();
        }
        return current;
    }

}
