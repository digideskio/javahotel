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
package com.gwtmodel.table.view;

import com.gwtmodel.table.FUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.rdef.FormLineContainer;

public class ValidateUtil {

    public static List<InvalidateMess> checkEmpty(IVModelData mData,
            List<IVField> listMFie, Set<IVField> ignoreV) {
        List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
        boolean ok = true;
        for (IVField f : listMFie) {
            if (!ignoreV.contains(f) && FUtils.isNullValue(mData, f)) {
                ok = false;
                errMess.add(new InvalidateMess(f, true, null));
            }
        }
        if (ok) {
            return null;
        }
        return errMess;
    }

    public static List<InvalidateMess> checkEmpty(IVModelData mData,
            List<IVField> listMFie) {
        return checkEmpty(mData, listMFie, new HashSet<IVField>());
    }

    public static List<InvalidateMess> checkEmpty(final FormLineContainer fo,
            List<IVField> listMFie) {
        IVModelData mData = new IVModelData() {

            public Object getF(IVField fie) {
                return fo.findLineView(fie).getValObj();
            }

            public void setF(IVField fie, Object o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isValid(IVField fie) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public List<IVField> getF() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Object getCustomData() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setCustomData(Object o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        return checkEmpty(mData, listMFie, new HashSet<IVField>());
    }

    private ValidateUtil() {
    }
}