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
package com.javahotel.nmvc.factories;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IHeaderListFactory;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.nmvc.common.VField;

class HeaderListFactory extends HelperFactory implements IHeaderListFactory {

    private ColListFactory cFactory;
    
    HeaderListFactory(ColListFactory cFactory) {
        this.cFactory = cFactory;
    }
    
    public VListHeaderContainer getVListHeaderContainer(IDataType dType) {
        DictData dicData = getDa(dType);
        List<ColTitle> coList = cFactory.getColList(dicData);
        String title = cFactory.getHeader(dicData);
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        for (ColTitle co : coList) {
            VListHeaderDesc he = new VListHeaderDesc(co.getCTitle(),
                    new VField(co.getF()));
            heList.add(he);
        }
        return new VListHeaderContainer(heList, title);
    }

}