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
package com.javahotel.nmvc.persist;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.PersistEventEnum;
import com.gwtmodel.table.slotmodel.SlotPublisherType;
import com.gwtmodel.table.slotmodel.ValidateActionType;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.persistrecord.IPersistRecord;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataUtil;

public class DataPersistLayer extends AbstractSlotContainer implements
        IDataPersistAction {

    private final IResLocator rI;
    private final SlotPublisherType publishList;
    private final DataType dType;
    private final PersistRecordFactory pFactory;

    private class ReadListDict implements IVectorList {

        private final ISlotSignalContext slContext;

        ReadListDict(ISlotSignalContext slContext) {
            this.slContext = slContext;
        }

        public void doVList(final List<? extends AbstractTo> val) {
            DataListType dataList = DataUtil.construct(val);
            slSignalContext.signal(publishList, dataList);
        }
    }
    
    private class AfterPersist implements IPersistResult {
        
        private final ValidateActionType.ValidateType vType;
        
        AfterPersist(ValidateActionType.ValidateType vType) {
            this.vType = vType;
        }

        public void success(PersistResultContext re) {
            Window.alert("after persist sucess");
        }
        
    }
    
    private class PersistRecord implements ISlotSignaller {
        
        private final ValidateActionType.ValidateType vType;
        
        PersistRecord(ValidateActionType.ValidateType vType) {
            this.vType = vType;
        }

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = callGetterModelData(GetActionEnum.ModelVDataPersist,dType);
            IPersistRecord persist = pFactory.getPersistDict(new DictData(dType.getdType()));
            int action = DataUtil.vTypetoAction(vType);
            RecordModel mo = DataUtil.toRecordModel(mData);
            persist.persist(action, mo, new AfterPersist(vType));
        }
        
    }

    private class ReadList implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            CommandParam co = rI.getR().getHotelCommandParam();
            co.setDict(dType.getdType());
            rI.getR().getList(RType.ListDict, co, new ReadListDict(slContext));
        }
    }
            

    public DataPersistLayer(IResLocator rI, PersistRecordFactory pFactory, DataType dType) {
        this.pFactory = pFactory;
        this.rI = rI;
        this.dType = dType;
        // create subscribers - ReadList
        registerSubscriber(PersistEventEnum.ReadList,dType,new ReadList());
        // persist subscriber
        registerSubscriber(PersistEventEnum.AddItem,dType,new PersistRecord(ValidateActionType.ValidateType.ADD));
        registerSubscriber(PersistEventEnum.RemoveItem,dType,new PersistRecord(ValidateActionType.ValidateType.REMOVE));
        registerSubscriber(PersistEventEnum.ChangeItem,dType,new PersistRecord(ValidateActionType.ValidateType.MODIF));
        
        // create publisher - ListRead
        publishList = registerPublisher(PersistEventEnum.ReadListSuccess,dType);
    }
    

    public void startPublish() {
    }

}