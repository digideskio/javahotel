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
package com.gwtmodel.table.slotmodel;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.mm.LogT;

/**
 * @author hotel
 *
 */
public class CustomStringDataTypeSlot extends CustomStringSlot {

    private final IDataType dType;

    public CustomStringDataTypeSlot(IDataType dType, String s) {
        super(s);
        this.dType = dType;
        assert dType != null : LogT.getT().cannotBeNull();
    }

    @Override
    public boolean eq(ISlotCustom o) {
        if (!(o instanceof CustomStringDataTypeSlot)) {
            return false;
        }
        CustomStringSlot c = (CustomStringSlot) o;
        if (!super.eq(c)) {
            return false;
        }
        CustomStringDataTypeSlot cc = (CustomStringDataTypeSlot) o;
        return dType.eq(cc.dType);
    }
}
