/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.WChoosedLine;

public class SlotSignalContextFactory {

    private static class SlotSignalContext implements ISlotSignalContext {

        private final SlotType slType;
        private final String changedValue;
        private final IValidateError validateError;
        private final IGWidget gwtWidget;
        private final DataListType dataList;
        private final WChoosedLine choosedLine;

        SlotSignalContext(SlotType slType, String changedValue,
                IValidateError validateError, IGWidget gwtWidget,
                DataListType dataList, WChoosedLine choosedLine) {
            this.slType = slType;
            this.changedValue = changedValue;
            this.validateError = validateError;
            this.gwtWidget = gwtWidget;
            this.dataList = dataList;
            this.choosedLine = choosedLine;
        }

        public WChoosedLine getChoosedLine() {
            return choosedLine;
        }

        public SlotType getSlType() {
            return slType;
        }

        public String getChangedValue() {
            return changedValue;
        }

        public IValidateError getValidateError() {
            return validateError;
        }

        public IGWidget getGwtWidget() {
            return gwtWidget;
        }

        public DataListType getDataList() {
            return dataList;
        }

    }

    public ISlotSignalContext construct(SlotType slType, DataListType dataList) {
        return new SlotSignalContext(slType, null, null, null, dataList, null);
    }

    public ISlotSignalContext construct(SlotType slType, DataListType dataList,
            WChoosedLine choosedLine) {
        return new SlotSignalContext(slType, null, null, null, dataList,
                choosedLine);
    }

    public ISlotSignalContext construct(SlotType slType) {
        return new SlotSignalContext(slType, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IGWidget gwtWidget) {
        return new SlotSignalContext(slType, null, null, gwtWidget, null, null);
    }

}
