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
package com.gwtmodel.table;

import java.util.List;

/**
 *
 * @author stanislaw.bartkowski@gmail.com
 */
class DataListType implements IDataListType {

    protected final List<IVModelData> dList;
    private final IVField comboFie;

    DataListType(List<IVModelData> dList, IVField comboFie) {
        this.dList = dList;
        this.comboFie = comboFie;
    }

    @Override
    public IVField comboField() {
        return comboFie;
    }

    @Override
    public void append(IVModelData vData) {
        dList.add(vData);
    }

    @Override
    public void remove(int row) {
        dList.remove(row);
    }

    @Override
    public List<IVModelData> getList() {
        return dList;
    }
}