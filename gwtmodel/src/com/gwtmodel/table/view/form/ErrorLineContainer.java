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
package com.gwtmodel.table.view.form;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.rdef.FormField;

class ErrorLineContainer {

    private final IGetCustomValues c;
    private List<FormField> el = new ArrayList<FormField>();

    ErrorLineContainer() {
        c = GwtGiniInjector.getI().getTableFactoriesContainer().getGetCustomValues();
    }

    void initErr() {
        el.clear();
    }

    void setEMess(FormField re, InvalidateMess m) {
        re.getELine().setGStyleName("dialog-empty-field", true);
        String e;
        if (m.isEmpty()) {
//            e = c.getCustomValue(IGetCustomValues.EMPTYFIELDERRORDEFAULT);
            e = MM.getL().EmptyFieldMessage();
        } else {
            e = m.getErrmess();
        }
        re.getELine().setInvalidMess(e);
        el.add(re);
    }

    void clearE() {
        for (FormField re : el) {
            re.getELine().setGStyleName("dialog-empty-field", false);
            re.getELine().setInvalidMess(null);
        }
        initErr();
    }
}