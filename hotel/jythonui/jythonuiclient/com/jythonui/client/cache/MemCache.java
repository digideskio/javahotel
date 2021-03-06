/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.cache;

import java.util.HashMap;
import java.util.Map;

import com.jythonui.client.interfaces.IMemCache;

public class MemCache implements IMemCache {

    private final Map<String, Object> m = new HashMap<String, Object>();

    @Override
    public void clear() {
        m.clear();

    }

    @Override
    public Object get(String key) {
        return m.get(key);
    }

    @Override
    public void put(String key, Object o) {
        m.put(key, o);
    }

}
