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
package com.jythonui.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hotel
 * 
 */
public class DialogVariables implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, FieldValue> vList = new HashMap<String, FieldValue>();
    private Map<String, ListOfRows> rowList = new HashMap<String, ListOfRows>();

    private Map<String, ListOfRows> enumList = new HashMap<String, ListOfRows>();

    private Map<String, DialogCheckVariables> checkVariables = new HashMap<String, DialogCheckVariables>();

    private Map<String, DateLineVariables> datelineVariables = new HashMap<String, DateLineVariables>();

    public FieldValue getValue(String fId) {
        return vList.get(fId);
    }

    public String getValueS(String fId) {
        FieldValue v = getValue(fId);
        if (v == null)
            return null;
        return v.getValueS();
    }

    public void setValue(String fId, FieldValue v) {
        vList.put(fId, v);
    }

    public void setValueB(String fId, boolean b) {
        FieldValue val = new FieldValue();
        val.setValue(b);
        vList.put(fId, val);
    }

    public void setValueS(String fId, String v) {
        FieldValue val = new FieldValue();
        val.setValue(v);
        vList.put(fId, val);
    }

    public void setValueL(String fId, long l) {
        FieldValue val = new FieldValue();
        val.setValue(l);
        vList.put(fId, val);
    }

    public List<String> getFields() {
        Set<String> s = vList.keySet();
        List<String> l = new ArrayList<String>();
        Iterator<String> i = s.iterator();
        while (i.hasNext()) {
            l.add(i.next());
        }
        return l;
    }

    public void setSecurityToken(String token) {
        setValueS(ICommonConsts.SECURITYTOKEN, token);
    }

    public void setLocale(String locale) {
        setValueS(ICommonConsts.GWT_LOCALE, locale);
    }

    /**
     * @return the rowList
     */
    public Map<String, ListOfRows> getRowList() {
        return rowList;
    }

    public void setRowList(String id, ListOfRows rows) {
        rowList.put(id, rows);
    }

    public ListOfRows getList(String id) {
        return rowList.get(id);
    }

    public Map<String, ListOfRows> getEnumList() {
        return enumList;
    }

    public void setEnumList(Map<String, ListOfRows> enumList) {
        this.enumList = enumList;
    }

    public Map<String, DialogCheckVariables> getCheckVariables() {
        return checkVariables;
    }

    public Map<String, DateLineVariables> getDatelineVariables() {
        return datelineVariables;
    }
       
}
