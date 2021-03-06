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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.AbstractListT.IGetList;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.util.PopupTip;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
abstract class AbstractField extends PopupTip implements IFormLineView {

	protected final List<ITouchListener> iTouch;
	protected Collection<IFormChangeListener> lC;
	protected final boolean isCheckBox;
	protected final boolean checkBoxVal;
	protected final IGetCustomValues cValues;
	protected final IVField v;
	private final String htmlName;
	protected final IFormFieldProperties pr;
	protected final AbstractListT listT;
	protected String cellTitle = null;

	AbstractField(IVField v, IFormFieldProperties pr) {
		this(v, true, pr, new ArrayList<ITouchListener>());
	}

	@Override
	public IVField getV() {
		return v;
	}

	private AbstractField(final IVField v, boolean checkenable, IFormFieldProperties pr, List<ITouchListener> iTouch) {
		assert v != null : LogT.getT().cannotBeNull();
		assert pr != null : LogT.getT().cannotBeNull();
		this.cValues = GwtGiniInjector.getI().getCustomValues();
		if (iTouch != null)
			this.iTouch = iTouch;
		else
			this.iTouch = new ArrayList<ITouchListener>();
		lC = null;
		checkBoxVal = checkenable;
		isCheckBox = true;
		this.pr = pr;
		this.htmlName = pr.getHtmlId();
		this.v = v;
		if (v.getType().getLi() == null) {
			listT = null;
		} else {
			IGetList iGet = new IGetList() {
				public List<IMapEntry> getL() {
					return v.getType().getLi().getList();
				}
			};
			listT = new AbstractListT(iGet) {
			};
		}
	}

	AbstractField(final IVField v, IFormFieldProperties pr, boolean checkenable) {
		this(v, checkenable, pr, null);
		assert v != null : LogT.getT().cannotBeNull();
	}

	@Override
	public void setGStyleName(final String sName, final boolean set) {
		if (set) {
			// 2013/05/28 : "add" instead of "set"
			getGWidget().addStyleName(sName);
		} else {
			getGWidget().removeStyleName(sName);
		}
	}

	protected class L implements ChangeListener {

		@Override
		public void onChange(Widget sender) {
			runOnChange(AbstractField.this, true);
		}
	}

	@SuppressWarnings("rawtypes")
	protected class ValueChange implements ValueChangeHandler {

		@Override
		public void onValueChange(ValueChangeEvent event) {
			runOnChange(AbstractField.this, true);
		}
	}

	protected class Touch implements KeyboardListener {

		private final ITouchListener iTouch;

		Touch(final ITouchListener iTouch) {
			this.iTouch = iTouch;
		}

		@Override
		public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			if (iTouch != null) {
				iTouch.onTouch();
			}

		}

		@Override
		public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			if (iTouch != null) {
				iTouch.onTouch();
			}
		}

		@Override
		public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			if (iTouch != null) {
				iTouch.onTouch();
			}
		}
	}

	@Override
	public void setOnTouch(final ITouchListener iTouch) {
		this.iTouch.add(iTouch);
	}

	@Override
	public void addChangeListener(final IFormChangeListener l) {
		if (lC == null) {
			lC = new ArrayList<IFormChangeListener>();
		}
		lC.add(l);
	}

	protected void runOnChange(IFormLineView i, boolean afterFocus) {
		if (lC == null) {
			return;
		}
		for (IFormChangeListener c : lC) {
			c.onChange(i, afterFocus);
		}
	}

	@Override
	public void setInvalidMess(String errmess) {
		setMessage(errmess);
	}

	@Override
	public Widget getGWidget() {
		return this;
	}

	@Override
	public void setHidden(boolean hidden) {
		this.getGWidget().setVisible(!hidden);
	}

	protected void onTouch() {
		// 2013/05/28 - make copy to avoid concurrent modifications
		List<ITouchListener> copyI = new ArrayList<ITouchListener>();
		copyI.addAll(iTouch);
		// now iterate over copy
		for (ITouchListener t : copyI) {
			t.onTouch();
		}
	}

	@Override
	public String getHtmlName() {
		if (!CUtil.EmptyS(htmlName)) {
			return htmlName;
		}
		return v.getId();
	}

	@Override
	public void setAttr(String attrName, String attrValue) {
		Element e = this.getWidget().getElement();
		e.setAttribute(attrName, attrValue);
	}

	@Override
	public void setCellTitle(String title) {
		this.cellTitle = title;
	}

	@Override
	public void setSuggestList(List<String> list) {
	}
	
	@Override
	public boolean isInvalid() {
		return false;
	}
	
	@Override
	public void replaceWidget(Widget w) {
		Utils.ReplaceWidgetNotImplements(v.getId());		
	}



}
