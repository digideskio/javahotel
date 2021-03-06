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

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormFieldProperties;
import com.gwtmodel.table.editw.ITouchListener;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.view.ewidget.gwt.richtextoolbar.googlerichbar.RichTextToolbar;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class ExtendTextBox extends AbstractField {

	private class EWidget implements IGWidget {

		private class SetRemoveTitle implements FocusListener {

			public void onFocus(Widget sender) {
			}

			public void onLostFocus(Widget sender) {
				EWidget.this.setTitle(true);
			}
		}

		private final TextBoxBase tBox;
		private final RichTextArea rArea;
		private final SuggestBox sBox;
		private final MultiWordSuggestOracle sOracle = new MultiWordSuggestOracle();
		private final Grid vP;

		private class R extends ReadR {

			R() {
				super(cValues);
			}

			@Override
			void setList(List<String> rList) {
				sOracle.addAll(rList);
			}
		}

		EWidget(EParam param) {
			if (param.isPassword()) {
				tBox = new PasswordTextBox();
				rArea = null;
				vP = null;
				sBox = null;
			} else {
				if (param.isIsRich()) {
					vP = new Grid(2, 1);
					vP.setStyleName("cw-RichText");
					rArea = new RichTextArea();
					RichTextToolbar r = new RichTextToolbar(rArea);
					rArea.setWidth("100%");
					vP.setWidget(0, 0, r);
					vP.setWidget(1, 0, rArea);
					tBox = null;
					sBox = null;
				} else {
					if (param.tBox == null) {
						tBox = param.isArea() ? new TextArea() : new TextBox();
					} else {
						tBox = param.tBox;
					}
					// tBox.addFocusHandler(new SetRemoveTitle());
					// TODO: cannot use FocusHandler because it is fired only
					// when focus is gained
					// Don't know how to handle focus lost
					// So use deprecated FocusListener
					tBox.addFocusListener(new SetRemoveTitle());
					if (param.suggestbox) {
						sBox = new SuggestBox(sOracle, tBox);
						if (param.iGet != null) {
							param.iGet.call(v, new R());
						}
						// tBox.addFocusHandler(new Focus());
					} else {
						sBox = null;
					}
					rArea = null;
					vP = null;
				}
			}
		}

		public Widget getGWidget() {
			if (rArea == null) {
				if (sBox != null) {
					return sBox;
				}
				return tBox;
			}
			return vP;
		}

		void setName(String nameId) {
			if (rArea == null)
				tBox.setName(nameId);
		}

		void setEnabled(boolean enabled) {
			if (rArea == null)
				tBox.setEnabled(enabled);
			else
				rArea.setEnabled(enabled);

		}

		private void setBoxTitle() {
			String s = tBox.getText();
			if (CUtil.EmptyS(cellTitle))
				tBox.setTitle(s);
			else
				tBox.setTitle(cellTitle);
		}

		void setFocus(boolean focus) {
			if (tBox != null)
				tBox.setFocus(focus);
			if (rArea != null)
				rArea.setFocus(focus);
		}

		void setText(String t) {
			if (rArea == null) {
				tBox.setText(t);
				setBoxTitle();
			} else {
				rArea.setHTML(t);
			}
		}

		void setTitle(boolean set) {
			if (tBox == null) {
				return;
			}
			if (!set) {
				tBox.setTitle(null);
				return;
			}
			setBoxTitle();
		}

		void addKeyboardListener(KeyboardListener k) {
			if (rArea == null) {
				tBox.addKeyboardListener(k);
			} else {
				rArea.addKeyboardListener(k);
			}
		}

		private void removeKeyboardListener(KeyboardListener k) {
			if (rArea == null) {
				tBox.removeKeyboardListener(k);
			} else {
				rArea.removeKeyboardListener(k);
			}
		}

		String getText() {
			if (rArea == null) {
				return tBox.getText();
			} else {
				return rArea.getHTML();
			}
		}

		void setReadOnly(boolean r) {
			if (rArea == null) {
				tBox.setReadOnly(r);
			} else {
				rArea.setEnabled(!r);
			}
		}

		void addChangeListener(ChangeListener c) {
			if (rArea == null) {
				tBox.addChangeListener(c);
			} else {
				// rArea.a
			}

		}
	}

	static class EParam {

		private final boolean password;
		private final boolean area;
		private final boolean suggestbox;
		private final boolean panel;
		private final boolean enable;
		private final boolean isRich;
		private final IGetDataList iGet;
		private final TextBoxBase tBox;

		EParam(boolean password, boolean panel, boolean area, boolean enable, boolean isRich, boolean suggestbox,
				IGetDataList iGet, TextBoxBase tBox) {
			this.password = password;
			this.panel = panel;
			this.area = area;
			this.enable = enable;
			this.isRich = isRich;
			this.suggestbox = suggestbox;
			this.iGet = iGet;
			this.tBox = tBox;
		}

		/**
		 * @return the password
		 */
		public boolean isPassword() {
			return password;
		}

		/**
		 * @return the area
		 */
		public boolean isArea() {
			return area;
		}

		/**
		 * @return the panel
		 */
		public boolean isPanel() {
			return panel;
		}

		/**
		 * @return the enable
		 */
		public boolean isEnable() {
			return enable;
		}

		/**
		 * @return the isRich
		 */
		public boolean isIsRich() {
			return isRich;
		}
	}

	protected final Widget wW;
	private final EWidget eW;
	protected final HorizontalPanel hPanel;
	// protected final CheckBox check;
	protected final boolean isArea;

	protected ExtendTextBox(IVField v, IFormFieldProperties pr, EParam param) {
		super(v, pr);
		this.isArea = param.isArea();
		eW = new EWidget(param);
		eW.setName(getHtmlName());
		if (param.isPanel()) {
			hPanel = new HorizontalPanel();
			hPanel.add(eW.getGWidget());
			wW = hPanel;
		} else {
			hPanel = null;
			wW = eW.getGWidget();
		}
		initWidget(wW);
	}

	private void changeS() {
		eW.setEnabled(true);
	}

	@Override
	public void setOnTouch(final ITouchListener iTouch) {
		if (iTouch == null) {
			// design gap: removing all listeners
			for (ITouchListener t : super.iTouch) {
				eW.removeKeyboardListener(new Touch(t));
			}
			super.iTouch.clear();
			return;
		}
		super.setOnTouch(iTouch);
		eW.addKeyboardListener(new Touch(iTouch));
	}

	@Override
	public void setValObj(Object o) {
		String va = (String) o;
		onTouch();
		eW.setText(va);
		eW.setTitle(true);
		// warning: 2011/09/09 (changed to this from null)
		runOnChange(this, false);
	}

	protected void setValAndFireChange(final String v) {
		setValObj(v);
		runOnChange(this, false);
	}

	@Override
	public Object getValObj() {
		return eW.getText();
	}

	@Override
	public void setReadOnly(final boolean r) {
		eW.setReadOnly(r);
	}

	public boolean validateField() {
		return true;
	}

	private class L implements ChangeListener {

		@Override
		public void onChange(Widget sender) {
			runOnChange(ExtendTextBox.this, true);
		}
	}

	@Override
	public void addChangeListener(final IFormChangeListener l) {
		super.addChangeListener(l);
		eW.addChangeListener(new L());
	}

	@Override
	public Widget getGWidget() {
		return this;
	}

	protected Widget getTextBox() {
		return eW.getGWidget();
	}

	@Override
	public void setInvalidMess(String errmess) {
		super.setInvalidMess(errmess);
		eW.setTitle(errmess == null);
	}

	@Override
	public void setCellTitle(String title) {
		this.cellTitle = title;
		eW.setBoxTitle();
	}

	@Override
	public void setFocus(boolean focus) {
		eW.setFocus(focus);
	}
}
