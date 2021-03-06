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
package com.gwtmodel.table.factories;

import com.gwtmodel.table.IVField;

public interface IGetCustomValues {

    IVField getSymForCombo();

    String IMAGEFORLISTHELP = "IMAGEFORLISTHEP";
    String IMAGEFOLDER = "IMAGEFOLDER";
    String RESOURCEFOLDER = "RESOURCEFOLDER";
    String YESVALUE = "YESVALUE";
    String NOVALUE = "NOVALUE";
    String COMMERROR = "COMMERROR";
    String COUNTCALLBACK = "COUNTCALLBACK";
    String QUESTION = "QUESTION";
    String INFO = "INFO";
    String DATEFORMAT = "DATEFORMAT";
    String DATETIMEFORMAT = "DATETIMEFORMAT";
    String HTMLPANELFORCHOOSELIST = "HTMLPANELFORCHOOSELIST";
    String NUMBERFORMAT0 = "NUMBERFORMAT0";
    String NUMBERFORMAT1 = "NUMBERFORMAT1";
    String NUMBERFORMAT2 = "NUMBERFORMAT2";
    String NUMBERFORMAT3 = "NUMBERFORMAT3";
    String NUMBERFORMAT4 = "NUMBERFORMAT4";
    String FINDIMAGE = "FIND";
    String FILTRIMAGE = "FILTR";
    String ADDITEMIMAGE = "ADDITEMIMAGE";
    String MODIFITEMIMAGE = "MODIFITEMIMAGE";
    String REMOVEITEMIMAGE = "REMOVEITEM";
    String SHOWITEMIMAGE = "SHOWITEMIMAGE";
    String PROPIMAGE = "PROPIMAGE";
    String CLOSEIMAGE = "CLOSEIMAGE";
    String EXPANDIMAGE = "EXPANDIMAGE";
    String JCOOKIEPREFIX = "JCOOKIEPREFIX";
    String HTMLPANELADDID = "HTMLPANELADDID";

    String VALUEYES = "Y";

    String getCustomValue(String key);

    boolean compareComboByInt();

    boolean addEmptyAsDefault();

    String getStandMessage(String key);
}
