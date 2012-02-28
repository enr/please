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

package com.atoito.please.uat;

import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.atoito.please.core.util.M;

/**
 * A very simple smoke test which fails if any exception is throwed.
 */
public class BasicFunctionalitySmokeUat extends BaseUat {

    @Test(dataProvider = "basic-functions", description = "base functionality is working")
    public void basicFunctionsAreWorking(String[] args) {
        M.info("basicFunctionsAreWorking: %s", Arrays.asList(args));
        runApplicationWithArgs(args);
    }

    /*
     * Creates arguments for basic calling
     */
    @DataProvider(name = "basic-functions")
    public Object[][] basicFunctions() {
        return new Object[][] { new Object[] { new String[] {} },
                new Object[] { new String[] { "no-such-operation" } }, new Object[] { new String[] { "reports" } },
                new Object[] { new String[] { "empty-op" } } };
    }

}


