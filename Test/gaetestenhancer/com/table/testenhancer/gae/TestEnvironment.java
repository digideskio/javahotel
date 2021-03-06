/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.table.testenhancer.gae;

import com.google.apphosting.api.ApiProxy;

import java.util.HashMap;
import java.util.Map;

class TestEnvironment implements ApiProxy.Environment {
    public String getAppId() {
        return "test";
    }

    public String getVersionId() {
        return "1.0";
    }

    public String getEmail() {
        throw new UnsupportedOperationException();
    }

    public boolean isLoggedIn() {
        throw new UnsupportedOperationException();
    }

    public boolean isAdmin() {
        throw new UnsupportedOperationException();
    }

    public String getAuthDomain() {
        throw new UnsupportedOperationException();
    }

    public String getRequestNamespace() {
        return "";
    }

    public Map<String, Object> getAttributes() {
        return new HashMap<String, Object>();
    }

    /* (non-Javadoc)
     * @see com.google.apphosting.api.ApiProxy.Environment#getRemainingMillis()
     */
    @Override
    public long getRemainingMillis() {
        // TODO Auto-generated method stub
        return 0;
    }

	@Override
	public String getModuleId() {
		// TODO Auto-generated method stub
		return null;
	}
}