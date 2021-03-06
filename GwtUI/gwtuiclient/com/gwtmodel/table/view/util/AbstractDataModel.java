/*
 *  Copyright 2016 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.gwtmodel.table.view.util;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.editw.FormLineContainer;
import com.gwtmodel.table.factories.IDataModelFactory;

import java.util.List;

/**
 *
 * @author hotel
 */
public abstract class AbstractDataModel implements IDataModelFactory {

    protected void copyData(List<IVField> li, IVModelData from, IVModelData to) {
        for (IVField f : li) {
            Object val = from.getF(f);
            to.setF(f, val);
        }
    }

    @Override
    public void copyFromPersistToModel(IDataType dType, IVModelData from,
            IVModelData to) {
        copyData(from.getF(), from, to);
    }

    @Override
    public void fromModelToPersist(IDataType dType, IVModelData from,
            IVModelData to) {
        copyData(from.getF(), from, to);
    }

    @Override
    public void fromViewToData(IDataType dType, FormLineContainer fContainer,
            IVModelData aTo) {
        FormUtil.copyFromViewToData(fContainer, aTo);
    }

    @Override
    public void fromDataToView(IDataType dType,
            PersistTypeEnum persistTypeEnum, IVModelData aFrom,
            FormLineContainer fContainer) {
        FormUtil.copyFromDataToView(aFrom, fContainer);
    }

}
