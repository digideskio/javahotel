/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.table;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style.OutlineStyle;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractCellTable.Style;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.view.client.SelectionModel;

/**
 * Important: it is copy and paste of DefaultCellTableBuilder The only
 * difference is to apply a span attribute
 * 
 * @param <T>
 */

class SpanCellBuilder<T> extends AbstractCellTableBuilder<T> {

    interface IGetSpanColValue<T> {
        int get(T rowNo, int colNo);
    }

    private final String evenRowStyle;
    private final String oddRowStyle;
    private final String selectedRowStyle;
    private final String cellStyle;
    private final String evenCellStyle;
    private final String oddCellStyle;
    private final String firstColumnStyle;
    private final String lastColumnStyle;
    private final String selectedCellStyle;

    private final IGetSpanColValue iSpan;

    SpanCellBuilder(AbstractCellTable<T> cellTable, IGetSpanColValue iSpan) {
        super(cellTable);

        // Cache styles for faster access.
        Style style = cellTable.getResources().style();
        evenRowStyle = style.evenRow();
        oddRowStyle = style.oddRow();
        selectedRowStyle = " " + style.selectedRow();
        cellStyle = style.cell();
        evenCellStyle = " " + style.evenRowCell();
        oddCellStyle = " " + style.oddRowCell();
        firstColumnStyle = " " + style.firstColumn();
        lastColumnStyle = " " + style.lastColumn();
        selectedCellStyle = " " + style.selectedRowCell();
        this.iSpan = iSpan;
    }

    @Override
    public void buildRowImpl(T rowValue, int absRowIndex) {

        // Calculate the row styles.
        SelectionModel<? super T> selectionModel = cellTable
                .getSelectionModel();
        boolean isSelected = (selectionModel == null || rowValue == null) ? false
                : selectionModel.isSelected(rowValue);
        boolean isEven = absRowIndex % 2 == 0;
        StringBuilder trClasses = new StringBuilder(isEven ? evenRowStyle
                : oddRowStyle);
        if (isSelected) {
            trClasses.append(selectedRowStyle);
        }

        // Add custom row styles.
        RowStyles<T> rowStyles = cellTable.getRowStyles();
        if (rowStyles != null) {
            String extraRowStyles = rowStyles.getStyleNames(rowValue,
                    absRowIndex);
            if (extraRowStyles != null) {
                trClasses.append(" ").append(extraRowStyles);
            }
        }

        // Build the row.
        TableRowBuilder tr = startRow();
        tr.className(trClasses.toString());

        // Build the columns.
        int columnCount = cellTable.getColumnCount();
        // added: SPAN
        int lastSpanCounter = 0;
        for (int curColumn = 0; curColumn < columnCount; curColumn++) {

            // SPAN: omit generating cell
            if (lastSpanCounter > 0) {
                lastSpanCounter--;
                continue;
            }

            Column<T, ?> column = cellTable.getColumn(curColumn);
            // Create the cell styles.
            StringBuilder tdClasses = new StringBuilder(cellStyle);
            tdClasses.append(isEven ? evenCellStyle : oddCellStyle);
            if (curColumn == 0) {
                tdClasses.append(firstColumnStyle);
            }
            if (isSelected) {
                tdClasses.append(selectedCellStyle);
            }
            // The first and last column could be the same column.
            if (curColumn == columnCount - 1) {
                tdClasses.append(lastColumnStyle);
            }

            // Add class names specific to the cell.
            Context context = new Context(absRowIndex, curColumn,
                    cellTable.getValueKey(rowValue));
            String cellStyles = column.getCellStyleNames(context, rowValue);
            if (cellStyles != null) {
                tdClasses.append(" " + cellStyles);
            }

            // Build the cell.
            HorizontalAlignmentConstant hAlign = column
                    .getHorizontalAlignment();
            VerticalAlignmentConstant vAlign = column.getVerticalAlignment();
            TableCellBuilder td = tr.startTD();

            // SPAN
            lastSpanCounter = iSpan.get(rowValue, curColumn);
            if (lastSpanCounter > 1) {
                td.attribute("colspan", Integer.toString(lastSpanCounter));
                lastSpanCounter--;
            }

            td.className(tdClasses.toString());
            if (hAlign != null) {
                td.align(hAlign.getTextAlignString());
            }
            if (vAlign != null) {
                td.vAlign(vAlign.getVerticalAlignString());
            }

            // Add the inner div.
            DivBuilder div = td.startDiv();
            div.style().outlineStyle(OutlineStyle.NONE).endStyle();

            // Render the cell into the div.
            renderCell(div, context, column, rowValue);

            // End the cell.
            div.endDiv();
            td.endTD();
        }

        // End the row.
        tr.endTR();
    }
}
