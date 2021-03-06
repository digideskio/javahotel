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
package com.gwtmodel.table.view.ewidget.gwt;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editw.IFormFieldProperties;

/**
 * @author hotel
 * 
 */
class LabelEdit extends ExtendTextBox {

	final private Label pL;

	public LabelEdit(IVField v, IFormFieldProperties pr, EParam param, final String lN) {
		super(v, pr, param);
		pL = new Label(lN);
		hPanel.setSpacing(1);
		hPanel.insert(pL, 0);
		HasVerticalAlignment.VerticalAlignmentConstant al = HasVerticalAlignment.ALIGN_BOTTOM;
		hPanel.setCellVerticalAlignment(getTextBox(), al);
	}
}
