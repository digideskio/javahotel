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
package com.javahotel.common.util;

import com.javahotel.common.toobject.DictionaryP;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ToStringList {

    private ToStringList() {
    }

    public static List<String> toL(
            final List<? extends DictionaryP> col) {
        List<String> out = new ArrayList<String>();
        for (DictionaryP d : col) {
            out.add(d.getName());
        }
        return out;
    }
}