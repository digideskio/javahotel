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
package com.javahotel.nmvc.validate;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotPublisherType;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.ValidateActionType;
import com.gwtmodel.table.slotmodel.ValidateActionType.ValidateActionEnum;
import com.gwtmodel.table.slotmodel.ValidateActionType.ValidateType;
import com.gwtmodel.table.util.ValidateUtil;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataUtil;

public class ValidateAction extends AbstractSlotContainer implements
        IDataValidateAction {

    private final IDataType dType;
    private final SlotPublisherType slPassed;
    private final SlotPublisherType slFailed;
    private final DictValidatorFactory valFactory;

    private class Validate implements ISignalValidate {

        public void failue(IErrorMessage errmess) {
            InvalidateFormContainer eContainer = DataUtil.convert(errmess);
            publishValidSignal(eContainer);
        }

        public void success() {
            publishValidSignal(null);
        }
    }

    private void publishValidSignal(InvalidateFormContainer errContainer) {
        if (errContainer == null) {
            publish(slPassed);
        } else {
            publish(slFailed, errContainer);
        }
    }

    private void validateE(ValidateType vType, IVModelData mData) {
        int action = DataUtil.vTypetoAction(vType);
        List<IVField> listMFie = DataUtil.constructEmptyList(dType, action);
        List<InvalidateMess> errMess = ValidateUtil.checkEmpty(mData, listMFie);
        if (errMess == null) {
            DataType da = (DataType) dType;
            IRecordValidator val = valFactory.getValidator(new DictData(da
                    .getdType()), true);
            RecordModel mo = DataUtil.toRecordModel(mData);
            val.validateS(action, mo, new Validate());
            return;
        }
        publishValidSignal(new InvalidateFormContainer(errMess));
    }

    private class ValidateA implements ISlotSignaller {

        private final ValidateType vType;

        ValidateA(ValidateType vType) {
            this.vType = vType;
        }

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = callGetterModelData(GetActionEnum.ModelVData,
                    dType);
            validateE(vType, mData);
        }
    }

    private void addVSubscriber(ValidateType vType) {
        registerSubscriber(ValidateActionEnum.Validate, vType, dType, new ValidateA(vType));

    }

    public ValidateAction(DictValidatorFactory valFactory, IDataType dType) {
        this.valFactory = valFactory;
        this.dType = dType;
        addVSubscriber(ValidateType.ADD);
        addVSubscriber(ValidateType.MODIF);
        addVSubscriber(ValidateType.REMOVE);
        slPassed = registerPublisher(ValidateActionEnum.ValidationPassed, dType);
        slFailed = registerPublisher(ValidateActionEnum.ValidatonFailed, dType);
    }

    public void startPublish() {
    }

}