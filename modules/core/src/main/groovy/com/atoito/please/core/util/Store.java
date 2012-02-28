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

import java.io.File;

import com.google.common.base.Preconditions;

/**
 * Utility class pertaining actions' properties storage.
 * 
 */
public class Store {

    private Store() {
    }

    public static File toFile(Object configured) {
        Object value = Preconditions.checkNotNull(configured);
        return new File(simplifyPath(value.toString()));
    }

    /**
     * Returns the lexically cleaned form of the path name, <i>usually</i> (but
     * not always) equivalent to the original. The following heuristics are
     * used:
     * 
     * <ul>
     * <li>empty string becomes .
     * <li>fold out ../ when possible
     * <li>fold out ./ when possible
     * <li>collapse multiple slashes
     * <li>delete trailing slashes (unless the path is just "/")
     * </ul>
     * 
     * These heuristics do not always match the behavior of the filesystem. In
     * particular, consider the path {@code a/../b}, which {@code simplifyPath}
     * will change to {@code b}. If {@code a} is a symlink to {@code x},
     * {@code a/../b} may refer to a sibling of {@code x}, rather than the
     * sibling of {@code a} referred to by {@code b}.
     * 
     * @since 10.0
     */
    private static String simplifyPath(String pathname) {
        if (pathname.length() == 0) {
            return ".";
        }
        char[] name = pathname.toCharArray();
        // In place, rewrite name to compress multiple /, eliminate ., and
        // process ..

        boolean rooted = (name[0] == '/');

        // invariants:
        // p points at beginning of path element we're considering.
        // q points just past the last path element we wrote (no slash).
        // dotdot points just past the point where .. cannot backtrack
        // any further (no slash).
        int firstNonSlash = rooted ? 1 : 0;
        int p = firstNonSlash;
        int q = firstNonSlash;
        int dotdot = firstNonSlash;
        while (p < name.length) {
            if (name[p] == '/') {
                /* null element */
                p++;
            } else if (name[p] == '.' && sep(name, p + 1)) {
                /* don't count the separator in case it is null */
                p += 1;
            } else if (name[p] == '.' && ((p + 1) < name.length && name[p + 1] == '.') && sep(name, p + 2)) {
                p += 2;
                if (q > dotdot) {
                    /* can backtrack */
                    while (--q > dotdot && name[q] != '/') {
                    }
                } else if (!rooted) {
                    /* /.. is / but ./../ is .. */
                    if (name[q] != name[0]
                            || (q != 0 && name.length >= q + 3 && name[0] == '.' && name[name.length - 1] != '/')) {
                        name[q++] = '/';
                    }
                    name[q++] = '.';
                    name[q++] = '.';
                    dotdot = q;
                }
            } else {
                /* real path element */
                if (name[q] != name[firstNonSlash] /*
                                                    * don't prefix ./b paths
                                                    * with /
                                                    */
                        || (name[q] == '.' && q != 0 && (sep(name, q + 1) || sep(name, q + 2)))) {
                    name[q++] = '/';
                }
                while ((q < name.length && p < name.length) && (name[q] = name[p]) != '/') {
                    p++;
                    q++;
                }
            }
        }
        return new String(name, 0, q);
    }

    private static boolean sep(char[] a, int pos) {
        return (pos >= a.length) || (a[pos] == '/');
    }
}


