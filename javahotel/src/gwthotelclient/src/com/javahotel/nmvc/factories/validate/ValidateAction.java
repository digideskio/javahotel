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
package com.javahotel.nmvc.factories.validate;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.types.DataType;

public class ValidateAction extends AbstractSlotContainer implements
        IDataValidateAction {

    private final EmptyColFactory eFactory = new EmptyColFactory();

    private class ValidateA implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            DataType da = (DataType) dType;
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetViewComposeModelEdited, dType);
            FormLineContainer fContainer = getGetterContainer(dType);
            if (!ValidateEmpty.validateE(slContainer, da,
                    slContext.getPersistType(), pData, fContainer,
                    eFactory.getEmptyCol(da, slContext.getPersistType()))) {
                return;
            }
            PersistTypeEnum action = slContext.getPersistType();
            ValidateOnServer.validateS(slContainer, da, action, pData);
        }
    }

    public ValidateAction(IDataType dType) {
        this.dType = dType;
        registerSubscriber(DataActionEnum.ValidateAction, dType,
                new ValidateA());
    }
}