/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.mail;

import java.util.List;
import java.util.Map;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.mailcommon.CListOfMailProperties;

/**
 *
 * @author perseus
 */
public class ListOfMailProperties extends CListOfMailProperties implements ICustomObject {


    public ListOfMailProperties(List<Map<String, String>> mList, String errMess) {
    	setmList(mList);
    	setErrMess(errMess);
    }

}