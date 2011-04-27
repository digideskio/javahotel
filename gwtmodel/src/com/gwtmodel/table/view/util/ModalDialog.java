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
package com.gwtmodel.table.view.util;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.MaxI;

abstract public class ModalDialog {

    private final DialogBox dBox;
    protected final VerticalPanel vP;
    private String title;
    private ICloseAction iClose = null;

    private class CloseClick implements ICloseAction {

        @Override
        public void onClose() {
            hide();
            if (iClose != null) {
                iClose.onClose();
            }
        }
    }

    protected abstract void addVP(VerticalPanel vp);

    public void setOnClose(ICloseAction iClose) {
        this.iClose = iClose;
    }

    public ModalDialog(String title) {
        vP = new VerticalPanel();
        this.title = title;
        dBox = new DialogBox(false, true);
    }

    public ModalDialog(VerticalPanel vP, String title) {
        this(vP, title, true);
    }

    public ModalDialog(VerticalPanel vP, String title, boolean modal) {
        this.vP = vP;
        this.title = title;
        dBox = new DialogBox(false, modal);
    }

    public ModalDialog(VerticalPanel vP) {
        this(vP, null, true);
    }

    public void setTitle(String title) {
        this.title = title;
        dBox.setText(title);
  }

    protected void create() {
        create(new CloseClick());
    }

    protected void create(ICloseAction i) {
        PopupUtil.addClose(vP, i, null, null);
        addVP(vP);
        dBox.setWidget(vP);
        dBox.setText(title);
    }
    

    /**
     * @return the dBox
     */
    public DialogBox getDBox() {
        return dBox;
    }

    public void show() {
        dBox.show();
    }

    public void hide() {
        dBox.hide();
    }

    public void show(final WSize w, final SolidPos sPos) {

        dBox.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                int maxwi = Window.getClientWidth();
                int maxhei = Window.getClientHeight();
                int t = w.getTop() + w.getHeight();
                int l = w.getLeft();

                int left = MaxI.min(maxwi - offsetWidth, l);
                int top = MaxI.min(maxhei - offsetHeight, t);
                if (sPos.getStartl() != -1) {
                    top = sPos.getStartl();
                }
                if (sPos.getStartcol() != -1) {
                    left = sPos.getStartcol();
                }
                if (top < 0) { 
                    top = 0;
                }
                dBox.setPopupPosition(left, top);
            }
        });
    }

    public void show(final WSize w) {
        show(w, new SolidPos());
    }

    public void show(final Widget w, SolidPos sPos) {
        show(new WSize(w), sPos);
    }

    public void show(final Widget w) {
        show(new WSize(w), new SolidPos());
    }
}