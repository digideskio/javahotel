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
package com.gwtmodel.table.listdataview;

import java.util.List;

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;

/**
 * @author hotel
 * 
 */
public class EditRowsSignal extends ChangeEditableRowsParam implements ICustomObject {

    public static final String EditSignal = "DATALIST_ENABLE_SIGNAL_ROW";


    /**
     * @param i
     * @param editable
     * @param eList
     */
    public EditRowsSignal(int i, boolean editable, List<IVField> eList) {
        super(i,editable,eList);
    }

}