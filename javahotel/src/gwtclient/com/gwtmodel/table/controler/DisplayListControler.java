/*
f * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmodel.AbstractSlotMediatorContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotListContainer;

class DisplayListControler extends AbstractSlotMediatorContainer implements IDataControler {

    private final CellId cellTableId;
    private final CellId controlId;
//    private final ISlotMediator slMediator;
    private final SlotListContainer slContainer;
    private final boolean startM;
    private final DisplayListControlerParam cParam;

    DisplayListControler(DisplayListControlerParam cParam) {
        this.cParam = cParam;
        IDataPersistAction persistA = cParam.getListParam().getPersistA();
        IHeaderListContainer heList = cParam.getListParam().getHeList();
        // create panel View
//        PanelViewFactory pViewFactory = cParam.gettFactories().getpViewFactory();
        IPanelView pView = pViewFactory.construct(cParam.getdType(), cParam.getPanelId());
        controlId = pView.addCellPanel(0, 0);
        cellTableId = pView.addCellPanel(1, 0);
        pView.createView();
        // persist layer
        // header list
        ListDataViewFactory lDataFactory = cParam.gettFactories().getlDataFactory();
        IListDataView daView = lDataFactory.construct(cParam.getdType());
        ControlButtonViewFactory bFactory = cParam.gettFactories().getbViewFactory();
        IControlButtonView bView = bFactory.construct(cParam.getdType(),
                cParam.getListButton());
        if (cParam.getMe() == null) {
//          slMediator = cParam.gettFactories().getSlotMediatorFactory().construct();
            startM = true;
        } else {
            slMediator = cParam.getMe();
            startM = false;
        }
        slContainer = slMediator.getSlContainer();

        slMediator.registerSlotContainer(cParam.getPanelId(), pView);
        slMediator.registerSlotContainer(-1, persistA);
        slMediator.registerSlotContainer(cellTableId, daView);
        slMediator.registerSlotContainer(controlId, bView);
        slMediator.registerSlotContainer(-1, cParam.getcControler());
        if (heList != null) {
            slMediator.registerSlotContainer(-1, heList);
        }
    }

    private class DrawListAction implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            slContainer.publish(DataActionEnum.DrawListAction, cParam.getdType(),
                    slContext.getDataList(), cParam.getwSize());
        }
    }

    @Override
    public void startPublish(CellId cellId) {
        if (startM) {
            slMediator.startPublish(cellId);
        }
        slContainer.registerSubscriber(DataActionEnum.ListReadSuccessSignal,
                cParam.getdType(), new DrawListAction());
        slContainer.registerRedirector(cParam.gettFactories().getSlTypeFactory().construct(
                DataActionEnum.RefreshAfterPersistActionSignal, cParam.getdType()),
                cParam.gettFactories().getSlTypeFactory().construct(
                DataActionEnum.ReadListAction, cParam.getdType()));
        // secondly publish
        slContainer.publish(DataActionEnum.ReadListAction, cParam.getdType(), cParam.getwSize());
        slContainer.publish(DataActionEnum.ReadHeaderContainer, cParam.getdType());
    }
//    @Override
//    public SlotListContainer getSlContainer() {
//        return slContainer;
//    }
}
