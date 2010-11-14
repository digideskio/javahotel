/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.rdef.DataListModelView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final IGwtTableView tableView;
//    private final IDataType dType;
    private final DataListModelView listView;

    private class DrawHeader implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            VListHeaderContainer listHeader = slContext.getListHeader();
            listView.setHeaderList(listHeader);
            tableView.setModel(listView);
        }
    }

    private class DrawList implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IDataListType dataList = slContext.getDataList();
            WSize wSize = slContext.getWSize();
            listView.setDataList(dataList);
            tableView.refresh(wSize);
        }
    }

    private class GetComboField implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVField comboF = listView.getComboField();
            return construct(dType, comboF);
        }
    }

    private ISlotSignalContext constructChoosedContext() {
        WChoosedLine w = tableView.getClicked();
        WSize wSize = null;
        IVModelData vData = null;
        if (w.isChoosed()) {
            wSize = w.getwSize();
            vData = tableView.getViewModel().getRow(w.getChoosedLine());
        }
        return construct(GetActionEnum.GetListLineChecked, dType, vData,
                wSize);
    }

    private class GetListData implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return constructChoosedContext();
        }
    }

    private class ClickList implements ICommand {

        @Override
        public void execute() {
            publish(DataActionEnum.TableLineClicked, dType, constructChoosedContext());
        }
    }

    ListDataView(GwtTableFactory gFactory, IDataType dType) {
        listView = new DataListModelView();
        this.dType = dType;
        tableView = gFactory.construct(new ClickList());
        // subscriber
        registerSubscriber(DataActionEnum.DrawListAction, dType, new DrawList());
        registerSubscriber(DataActionEnum.ReadHeaderContainerSignal, dType,
                new DrawHeader());
        // caller
        registerCaller(GetActionEnum.GetListLineChecked, dType,
                new GetListData());
        registerCaller(GetActionEnum.GetListComboField, dType, new GetComboField());
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, tableView);
    }
}
