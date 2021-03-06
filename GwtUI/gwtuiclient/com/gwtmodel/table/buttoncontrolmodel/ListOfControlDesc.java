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
package com.gwtmodel.table.buttoncontrolmodel;

import java.util.ArrayList;
import java.util.List;

public class ListOfControlDesc {

    private final List<ControlButtonDesc> cList = new ArrayList<ControlButtonDesc>();
    private final String htmlFormat;

    public ListOfControlDesc(List<ControlButtonDesc> cList, String htmlFormat) {
        for (ControlButtonDesc c : cList) {
            this.cList.add(c);
        }
        this.htmlFormat = htmlFormat;
    }

    public ListOfControlDesc(List<ControlButtonDesc> cList) {
        this(cList, null);
    }

    public List<ControlButtonDesc> getcList() {
        return cList;
    }

    /**
     * @return the htmlFormat
     */
    public String getHtmlFormat() {
        return htmlFormat;
    }
}
