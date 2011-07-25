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
package com.javahotel.nmvc.factories.booking;

import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IOneList;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.nmvc.ewidget.EWidgetFactory;

/**
 * @author hotel
 * 
 */
class CustomBookingElem extends AbstractSlotContainer {

    private final ICallContext iContext;
    private final IResLocator rI;
    private final EWidgetFactory eFactory;
    private final BookElementRefreshPayment fPayment;
    private final ISlotable mainSlo;

    CustomBookingElem(ICallContext iContext, ISlotable mainSlo) {
        this.iContext = iContext;
        this.dType = iContext.getDType();
        registerSubscriber(dType, new VField(BookElemP.F.resObject),
                new ChangeRoom());
        rI = HInjector.getI().getI();
        eFactory = HInjector.getI().getEWidgetFactory();
        this.mainSlo = mainSlo;
        fPayment = new BookElementRefreshPayment(dType, this, iContext, mainSlo);
    }

    private class ReadStandard implements IOneList<AbstractTo> {

        private final IFormLineView sView;

        ReadStandard(IFormLineView sView) {
            this.sView = sView;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.javahotel.client.rdata.RData.IOneList#doOne(java.lang.Object)
         */
        @Override
        public void doOne(AbstractTo val) {
            RoomStandardP r = (RoomStandardP) val;
            eFactory.setComboDictList(sView, r.getServices());
        }
    }

    private class ReadRoom implements IOneList<AbstractTo> {

        private final IFormLineView sView;

        ReadRoom(IFormLineView sView) {
            this.sView = sView;
        }

        @Override
        public void doOne(AbstractTo val) {
            ResObjectP r = (ResObjectP) val;
            DictionaryP stand = r.getRStandard();
            String standName = stand.getName();
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomStandard);
            p.setRecName(standName);
            rI.getR().getOne(RType.ListDict, p, new ReadStandard(sView));
        }

    }

    private class ChangeRoom implements ISlotSignaller {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.gwtmodel.table.slotmodel.ISlotSignaller#signal(com.gwtmodel.table
         * .slotmodel.ISlotSignalContext)
         */
        @Override
        public void signal(ISlotSignalContext slContext) {
            IFormLineView iView = slContext.getChangedValue();
            String res = (String) iView.getValObj();
            if (res == null) {
                return;
            }
            FormLineContainer fContainer = getGetterContainer(dType);
            IFormLineView sView = fContainer.findLineView(new VField(
                    BookElemP.F.service));
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomObjects);
            p.setRecName(res);
            rI.getR().getOne(RType.ListDict, p, new ReadRoom(sView));
        }
    }

}