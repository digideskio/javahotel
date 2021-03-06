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

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;

/**
 *
 * @author hotel
 */
public class SetVPanelGwt {

    private final VerticalPanel vPanel;

    public SetVPanelGwt() {
        vPanel = new VerticalPanel();
    }

    /**
     * @return the vPanel
     */
    public VerticalPanel getvPanel() {
        return vPanel;
    }

    private class SetGwt implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            getvPanel().add(w);
        }
    }

    public ISlotListener constructSetGwt() {
        return new SetGwt();
    }

    public GWidget constructGWidget() {
        return new GWidget(getvPanel());
    }
}
