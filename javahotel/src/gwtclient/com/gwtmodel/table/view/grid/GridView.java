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
package com.gwtmodel.table.view.grid;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

class GridView implements IGridView {

    private final GridViewType gType;
    private final EditWidgetFactory wFactory;
    private final Grid g;
    private int rowNo = -1;
    private int colNo = -1;
    private List<String> colTitles = null;
    private List<String> rowTitles = null;
    private String rowTitle = null;
    private S synch = new S();

    private void drawCols() {
        if (colTitles == null) {
            return;
        }
        if (!gType.isColHeaders()) {
            return;
        }
        for (int i = 0; i < colNo; i++) {
            C c = getC(-1, i);
            g.setText(c.row, c.col, colTitles.get(i));
        }
    }

    private void drawRows() {
        if (rowTitles == null) {
            return;
        }
        if (!gType.isRowBeginning()) {
            return;
        }
        for (int i = 0; i < rowNo; i++) {
            C c = getC(i, -1);
            g.setText(c.row, c.col, rowTitles.get(i));
        }
    }

    private void drawTitle() {
        if (rowTitle == null) {
            return;
        }
        g.setText(0, 0, rowTitle);
    }

    private class S extends SynchronizeList {

        S() {
            super(2);
        }

        @Override
        protected void doTask() {
            C c = getC(rowNo, colNo);
            g.resize(c.row, c.col);
            drawCols();
            drawRows();
            drawTitle();
        }

    }

    GridView(EditWidgetFactory wFactory, GridViewType gType) {
        this.gType = gType;
        this.wFactory = wFactory;
        this.g = new Grid();
    }

    private class NumerW extends Composite {

        final IFormLineView iF;

        NumerW(IFormLineView iF) {
            this.iF = iF;
            initWidget(iF.getGWidget());
        }
    }

    private class C {

        int row;
        int col;
    }

    private C getC(int row, int col) {
        C c = new C();
        if (gType.isHorizontal()) {
            c.row = gType.isColHeaders() ? row + 1 : row;
            c.col = gType.isRowBeginning() ? col + 1 : col;
        } else {
            c.row = gType.isColHeaders() ? col + 1 : col;
            c.col = gType.isRowBeginning() ? row + 1 : row;
        }
        return c;
    }

    private Widget getW(int r, int c) {
        C co = getC(r, c);
        Widget w = g.getWidget(co.row, co.col);
        if (w == null) {
            w = null;
            switch (gType.getgType()) {
            case BOOLEAN:
                CheckBox ce = new CheckBox();
                w = ce;
                break;
            case DECIMAL:
                IFormLineView nView = wFactory.contructCalculatorNumber();
                NumerW nu = new NumerW(nView);
                w = nu;
                break;
            }
            g.setWidget(co.row, co.col, w);
        }
        return w;
    }

    public Object getCell(int row, int c) {
        Widget w = getW(row, c);
        switch (gType.getgType()) {
        case BOOLEAN:
            CheckBox ce = (CheckBox) w;
            boolean b = ce.isChecked();
            return new Boolean(b);
        case DECIMAL:
            NumerW nw = (NumerW) w;
            IFormLineView i = nw.iF;
            BigDecimal bi = i.getDecimal();
            return bi;
        }
        return null;
    }

    public void setColNo(int colNo) {
        this.colNo = colNo;
        synch.signalDone();
    }

    public void setCols(String rowTitle, List<String> cols) {
        this.colTitles = cols;
        if (this.colNo == -1) {
            setColNo(colTitles.size());
        }
    }

    public void setReadOnly(boolean readOnly) {
        for (int r = 0; r < rowNo; r++) {
            for (int c = 0; c < colNo ; c++) {
                Widget w = getW(r, c);
                switch (gType.getgType()) {
                    case BOOLEAN:
                        CheckBox ce = (CheckBox) w;
                        ce.setEnabled(!readOnly);
                        break;
                    case DECIMAL:
                        NumerW nw = (NumerW) w;
                        IFormLineView i = nw.iF;
                        i.setReadOnly(readOnly);
                        break;
                }
            }
        }
    }

    public void setRowBeginning(List<String> rows) {
        this.rowTitles = rows;
        if (this.rowNo == -1) {
            setRowNo(rows.size());
        }
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
        synch.signalDone();
    }

    public void setRowVal(int row, int c, Object o) {
        Widget w = getW(row, c);
        switch (gType.getgType()) {
        case BOOLEAN:
            Boolean b = (Boolean) o;
            CheckBox ce = (CheckBox) w;
            ce.setChecked(b.booleanValue());
            break;
        case DECIMAL:
            NumerW nw = (NumerW) w;
            IFormLineView i = nw.iF;
            BigDecimal bi = (BigDecimal) o;
            i.setDecimal(bi);
            break;
        }
    }

    public Widget getGWidget() {
        return g;
    }

}