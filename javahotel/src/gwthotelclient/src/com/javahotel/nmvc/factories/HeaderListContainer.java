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
package com.javahotel.nmvc.factories;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.types.DataType;

class HeaderListContainer extends AbstractSlotContainer implements
        IHeaderListContainer {

    private final VListHeaderContainer vHeader;

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, vHeader);
    }

    HeaderListContainer(IDataType dType,ColListFactory cList) {
        this.dType = dType;
        DataType da = (DataType) dType;
        String title = cList.getTitle(da);
        List<VListHeaderDesc> vList = cList.getColList(da);
        vHeader = new VListHeaderContainer(vList, title);
    }
}