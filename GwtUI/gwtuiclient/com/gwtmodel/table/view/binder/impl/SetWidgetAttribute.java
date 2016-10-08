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
package com.gwtmodel.table.view.binder.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.binder.WidgetTypes;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DecimalUtils;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.view.binder.ISetWidgetAttribute;
import com.gwtmodel.table.view.util.polymer.PolymerUtil;
import com.vaadin.polymer.PolymerWidget;
import com.vaadin.polymer.iron.widget.IronIcon;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperDialog;
import com.vaadin.polymer.paper.widget.PaperDialogScrollable;
import com.vaadin.polymer.paper.widget.PaperDrawerPanel;
import com.vaadin.polymer.paper.widget.PaperDropdownMenu;
import com.vaadin.polymer.paper.widget.PaperFab;
import com.vaadin.polymer.paper.widget.PaperHeaderPanel;
import com.vaadin.polymer.paper.widget.PaperIconButton;
import com.vaadin.polymer.paper.widget.PaperIconItem;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.paper.widget.PaperItem;
import com.vaadin.polymer.paper.widget.PaperMaterial;
import com.vaadin.polymer.paper.widget.PaperMenu;
import com.vaadin.polymer.paper.widget.PaperProgress;
import com.vaadin.polymer.paper.widget.PaperTabs;
import com.vaadin.polymer.paper.widget.PaperTextarea;
import com.vaadin.polymer.paper.widget.PaperToolbar;

@SuppressWarnings("unchecked")
public class SetWidgetAttribute implements ISetWidgetAttribute {

	private interface IVisitor<T extends Widget> {
		void visit(T w, String k, String v, boolean bv, double dv);
	}

	private final static IVisitor<Widget> argW = new IVisitor<Widget>() {

		@Override
		public void visit(Widget w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRHEIGHT))
				w.setHeight(v);
			else if (k.equals(IConsts.ATTRSIZE)) {
				String[] pp = v.split(" ");
				if (pp.length == 2)
					w.setSize(pp[0], pp[1]);
			} else if (k.equals(IConsts.ATTRPIXELSIZE)) {
				String[] pp = v.split(" ");
				if (pp.length == 2)
					w.setPixelSize(CUtil.toInteger(pp[0]), CUtil.toInteger(pp[1]));
			} else if (k.equals(IConsts.ATTRSTYLENAME))
				w.setStyleName(v);
			else if (k.equals(IConsts.ATTRSTYLEPRIMARYNAME))
				w.setStylePrimaryName(v);
			else if (k.equals(IConsts.ATTRVISIBLE))
				w.setVisible(bv);
			else if (k.equals(IConsts.ATTRTITLE))
				w.setTitle(v);
			else if (k.equals(IConsts.ATTRWIDTH))
				w.setWidth(v);
			else if (k.equals(IConsts.ATTRADDSTYLENAMES)) {
				String sa[] = v.split(" ");
				for (String a : sa)
					w.addStyleName(a);
			}

		}
	};

	private static final IVisitor<Label> argL = new IVisitor<Label>() {

		@Override
		public void visit(Label l, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRTEXT))
				l.setText(v);
		}
	};

	private static final IVisitor<FocusWidget> argF = new IVisitor<FocusWidget>() {

		@Override
		public void visit(FocusWidget w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTREENABLED))
				w.setEnabled(bv);
		}
	};

	private static final IVisitor<ButtonBase> argbaseB = new IVisitor<ButtonBase>() {

		@Override
		public void visit(ButtonBase w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRTEXT))
				w.setText(v);
			else if (k.equals(IConsts.ATTRHTML))
				w.setHTML(v);
		}

	};

	private static final IVisitor<PolymerWidget> polymerWidgetG = new IVisitor<PolymerWidget>() {

		@Override
		public void visit(PolymerWidget w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRATRIBUTES))
				w.setAttributes(v);
			else if (k.equals(IConsts.ATTRBOOLEANATTRIBUTE)) {
				String va[] = v.split(" ");
				for (String iv : va) {
					String[] bvv = iv.split(":");
					w.setBooleanAttribute(bvv[0], Utils.toB(bvv[1]));
				}
			} else if (k.equals(IConsts.ATTRDISABLED))
				w.setDisabled(bv);
			else if (k.equals(IConsts.ATTRID))
				w.setId(v);
			else if (k.equals(IConsts.ATTRNAME))
				w.setName(v);
			else if (k.equals(IConsts.ATTRNOINK))
				w.setNoink(bv);
			else if (k.equals(IConsts.ATTRSTYLE))
				w.setStyle(v);
			else if (k.equals(IConsts.ATTRTABINDEX))
				w.setTabindex(CUtil.getInteger(v));
			else if (k.equals(IConsts.ATTRCLASS))
				w.setClass(v);
			else if (k.equals(IConsts.ATTRNOCINK))
				w.setNoink(bv);
			else if (k.equals(IConsts.ATTRARIALABEL))
				w.setAriaLabel(v);
		}

	};

	private static final IVisitor<PaperIconItem> paperIconItemG = new IVisitor<PaperIconItem>() {

		@Override
		public void visit(PaperIconItem w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
		}

	};

	private static final IVisitor<IronIcon> ironIconG = new IVisitor<IronIcon>() {

		@Override
		public void visit(IronIcon w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRICON))
				w.setIcon(v);
			else if (k.equals(IConsts.ATTRSRC))
				w.setSrc(v);
			else if (k.equals(IConsts.ATTRTHEME))
				w.setTheme(v);
		}

	};

	private static final IVisitor<PaperButton> paperbuttonG = new IVisitor<PaperButton>() {

		@Override
		public void visit(PaperButton w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
			else if (k.equals(IConsts.ATTRACTIVE))
				w.setActive(bv);
			else if (k.equals(IConsts.ATTRRAISED))
				w.setRaised(bv);
			else if (k.equals(IConsts.ATTRELEVATION))
				w.setElevation(v);
			else if (k.equals(IConsts.ATTRARIAACTIVEATTRIBUTE))
				w.setAriaActiveAttribute(v);
			else if (k.equals(IConsts.ATTRELEVATIONFLOAT))
				w.setElevation(dv);
			else if (k.equals(IConsts.ATTRSTOPKEYBORADFROMPROPAGATION))
				w.setStopKeyboardEventPropagation(bv);
			else if (k.equals(IConsts.ATTRRECEIVEDFOCUSFROMKEYBOARD))
				w.setReceivedFocusFromKeyboard(bv);
			else if (k.equals(IConsts.ATTRKEYEVENTTARGET))
				w.setKeyEventTarget(v);
		}
	};

	private static final IVisitor<PaperItem> paperItemG = new IVisitor<PaperItem>() {

		@Override
		public void visit(PaperItem w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
			else if (k.equals(IConsts.ATTRACTIVE))
				w.setActive(bv);
			else if (k.equals(IConsts.ATTRARIAACTIVEATTRIBUTE))
				w.setAriaActiveAttribute(v);
			else if (k.equals(IConsts.ATTRSTOPKEYBORADFROMPROPAGATION))
				w.setStopKeyboardEventPropagation(bv);
			else if (k.equals(IConsts.ATTRRECEIVEDFOCUSFROMKEYBOARD))
				w.setReceivedFocusFromKeyboard(bv);
			else if (k.equals(IConsts.ATTRKEYEVENTTARGET))
				w.setKeyEventTarget(v);
		}

	};

	private static final IVisitor<PaperHeaderPanel> paperHeaderG = new IVisitor<PaperHeaderPanel>() {

		@Override
		public void visit(PaperHeaderPanel w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRATTOP))
				w.setAtTop(bv);
			else if (k.equals(IConsts.ATTRMODE))
				w.setMode(v);
			else if (k.equals(IConsts.ATTRSHADOW))
				w.setMode(v);
			else if (k.equals(IConsts.ATTRTALLCLASS))
				w.setTallClass(v);
		}
	};

	private static final IVisitor<PaperToolbar> paperToolbarG = new IVisitor<PaperToolbar>() {

		@Override
		public void visit(PaperToolbar w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRBOTTOMJUSTIFY))
				w.setBottomJustify(v);
			else if (k.equals(IConsts.ATTRJUSTIFY))
				w.setJustify(v);
			else if (k.equals(IConsts.ATTRMIDDLEJUSTIFY))
				w.setMiddleJustify(v);
		}
	};

	private static final IVisitor<Image> imageG = new IVisitor<Image>() {

		@Override
		public void visit(Image w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRURL))
				w.setUrl(v);
			else if (k.equals(IConsts.ATTRALTTEXT))
				w.setAltText(v);
		}

	};

	private static final IVisitor<PaperIconButton> papericonG = new IVisitor<PaperIconButton>() {

		@Override
		public void visit(PaperIconButton w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
			else if (k.equals(IConsts.ATTRICON))
				w.setIcon(v);
			else if (k.equals(IConsts.ATTRSRC))
				w.setSrc(v);

		}
	};

	private static final IVisitor<PaperCheckbox> papercheckboxG = new IVisitor<PaperCheckbox>() {

		@Override
		public void visit(PaperCheckbox w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
			else if (k.equals(IConsts.ATTRACTIVE))
				w.setActive(bv);
			else if (k.equals(IConsts.ATTRCHECKED))
				w.setChecked(bv);
			else if (k.equals(IConsts.ATTRINVALID))
				w.setInvalid(bv);
			else if (k.equals(IConsts.ATTRVALIDATOR))
				w.setValidator(v);
			else if (k.equals(IConsts.ATTRVALIDATORTYPE))
				w.setValidatorType(v);
			else if (k.equals(IConsts.ATTRVALUE))
				w.setValue(v);
		}

	};

	private static final IVisitor<PaperDrawerPanel> paperdrawerpanelG = new IVisitor<PaperDrawerPanel>() {

		@Override
		public void visit(PaperDrawerPanel w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRDEFAULTSELECTED))
				w.setDefaultSelected(v);
			else if (k.equals(IConsts.ATTRDISABLEEDGESWIPE))
				w.setDisableEdgeSwipe(bv);
			else if (k.equals(IConsts.ATTRDRAGGING))
				w.setDragging(bv);
			else if (k.equals(IConsts.ATTRDRAWERTOGGLEATTRIBUTE))
				w.setDrawerToggleAttribute(v);
			else if (k.equals(IConsts.ATTRDRAWEWIDTH))
				w.setDrawerWidth(v);
			else if (k.equals(IConsts.ATTREDGESWUPESENSITIVITY))
				w.setEdgeSwipeSensitivity(dv);
			else if (k.equals(IConsts.ATTREDGESWUPESENSITIVITYPIXELS))
				w.setEdgeSwipeSensitivity(v);
			else if (k.equals(IConsts.ATTRFORCENARROW))
				w.setForceNarrow(bv);
			else if (k.equals(IConsts.ATTRHASTRANSFORM))
				w.setHasTransform(bv);
			else if (k.equals(IConsts.ATTRHADWILLCHANGE))
				w.setHasWillChange(bv);
			else if (k.equals(IConsts.ATTRNARROW))
				w.setNarrow(bv);
			else if (k.equals(IConsts.ATTRPEEKING))
				w.setPeeking(bv);
			else if (k.equals(IConsts.ATTRRESPONSICEWIDTH))
				w.setResponsiveWidth(v);
			else if (k.equals(IConsts.ATTRRIGHTDRAWER))
				w.setRightDrawer(bv);
			else if (k.equals(IConsts.ATTRSELECTED))
				w.setSelected(v);

		}
	};

	private static final IVisitor<PaperDialog> paperdialogG = new IVisitor<PaperDialog>() {

		@Override
		public void visit(PaperDialog w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRANIMATIONCONFIG))
				w.setAnimationConfig(v);
			else if (k.equals(IConsts.ATTRAUTOFITONATTACH))
				w.setAutoFitOnAttach(bv);
			else if (k.equals(IConsts.ATTRCANCELED))
				w.setCanceled(bv);
			else if (k.equals(IConsts.ATTRETRYANIMATION))
				w.setEntryAnimation(v);
			else if (k.equals(IConsts.ATTREXITANIMATION))
				w.setExitAnimation(v);
			else if (k.equals(IConsts.ATTRFITINFO))
				w.setFitInto(v);
			else if (k.equals(IConsts.ATTRMODAL))
				w.setModal(bv);
			else if (k.equals(IConsts.ATTRNOAUTOFOCUS))
				w.setNoAutoFocus(bv);
			else if (k.equals(IConsts.ATTRNOCANCELONESCKEY))
				w.setNoCancelOnEscKey(bv);
			else if (k.equals(IConsts.ATTRNOCANCELONESCKEY))
				w.setNoCancelOnOutsideClick(bv);
			else if (k.equals(IConsts.ATTROPENED))
				w.setOpened(bv);
			else if (k.equals(IConsts.ATTRSIZINGTARGET))
				w.setSizingTarget(v);
			else if (k.equals(IConsts.ATTRWITHBACKDROP))
				w.setWithBackdrop(bv);

		}
	};

	private static final IVisitor<PaperDialogScrollable> paperdialogscrollableG = new IVisitor<PaperDialogScrollable>() {

		@Override
		public void visit(PaperDialogScrollable w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRDIALOGELEMENT))
				w.setDialogElement(v);
		}
	};

	private static final IVisitor<PaperDropdownMenu> paperdropdownmenuG = new IVisitor<PaperDropdownMenu>() {

		@Override
		public void visit(PaperDropdownMenu w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRACTIVE))
				w.setActive(bv);
			else if (k.equals(IConsts.ATTRALWAYSFLOATLABEL))
				w.setAlwaysFloatLabel(bv);
			else if (k.equals(IConsts.ATTRARIAACTIVEATTRIBUTE))
				w.setAriaActiveAttribute(v);
			else if (k.equals(IConsts.ATTRHORIZONTALALIGN))
				w.setHorizontalAlign(v);
			else if (k.equals(IConsts.ATTRINVALID))
				w.setInvalid(bv);
			else if (k.equals(IConsts.ATTRKEYBINDINGS))
				w.setKeyBindings(v);
			else if (k.equals(IConsts.ATTRKEYEVENTTARGET))
				w.setKeyEventTarget(v);
			else if (k.equals(IConsts.ATTRLABEL))
				w.setLabel(v);
			else if (k.equals(IConsts.ATTRNOANIMATION))
				w.setNoAnimations(bv);
			else if (k.equals(IConsts.ATTRNOLABELFLOAT))
				w.setNoLabelFloat(bv);
			else if (k.equals(IConsts.ATTROPENED))
				w.setOpened(bv);
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
			else if (k.equals(IConsts.ATTRPLACEHOLDER))
				w.setPlaceholder(v);
			else if (k.equals(IConsts.ATTRRECEIVEDFOCUSFROMKEYBOARD))
				w.setReceivedFocusFromKeyboard(bv);
			else if (k.equals(IConsts.ATTRREQUIRED))
				w.setRequired(bv);
			else if (k.equals(IConsts.ATTRSELECTEDITEM))
				w.setSelectedItem(v);
			else if (k.equals(IConsts.ATTRSELECTITEMLABEL))
				w.setSelectedItem(v);
			else if (k.equals(IConsts.ATTRSTOPKEYBORADFROMPROPAGATION))
				w.setStopKeyboardEventPropagation(bv);
			else if (k.equals(IConsts.ATTRVALIDATOR))
				w.setValidator(v);
			else if (k.equals(IConsts.ATTRVALIDATORTYPE))
				w.setValidatorType(v);
			else if (k.equals(IConsts.ATTRVALUE))
				w.setValue(v);
			else if (k.equals(IConsts.ATTRVERTICALALIGN))
				w.setVerticalAlign(v);

		}
	};

	private static final IVisitor<PaperMenu> papermenuG = new IVisitor<PaperMenu>() {

		@Override
		public void visit(PaperMenu w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRACTIVATEEVENT))
				w.setActivateEvent(v);
			else if (k.equals(IConsts.ATTRFORITEMTITLE))
				w.setAttrForItemTitle(v);
			else if (k.equals(IConsts.ATTRATTRFORSELECTED))
				w.setAttrForSelected(v);
			else if (k.equals(IConsts.ATTRFOCUSEDITEM))
				w.setFocusedItem(v);
			else if (k.equals(IConsts.ATTRITEMS))
				w.setItems(v);
			else if (k.equals(IConsts.ATTRKEYBINDINGS))
				w.setKeyBindings(v);
			else if (k.equals(IConsts.ATTRMULTI))
				w.setMulti(bv);
			else if (k.equals(IConsts.ATTRSELECTED))
				w.setSelected(v);
			else if (k.equals(IConsts.ATTRSELECTEDATTRIBUTE))
				w.setSelectedAttribute(v);
			else if (k.equals(IConsts.ATTRSELECTEDITEM))
				w.setSelectedItem(v);
			else if (k.equals(IConsts.ATTRSELECTEDITEMS))
				w.setSelectedItems(v);
			else if (k.equals(IConsts.ATTRSELECTEDVALUES))
				w.setSelectedValues(v);
		}
	};

	private static final IVisitor<PaperTabs> papertabsG = new IVisitor<PaperTabs>() {

		@Override
		public void visit(PaperTabs w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRACTIVATEEVENT))
				w.setActivateEvent(v);
			else if (k.equals(IConsts.ATTRFORITEMTITLE))
				w.setAttrForItemTitle(v);
			else if (k.equals(IConsts.ATTRATTRFORSELECTED))
				w.setAttrForSelected(v);
			else if (k.equals(IConsts.ATTRFOCUSEDITEM))
				w.setFocusedItem(v);
			else if (k.equals(IConsts.ATTRITEMS))
				w.setItems(v);
			else if (k.equals(IConsts.ATTRKEYBINDINGS))
				w.setKeyBindings(v);
			else if (k.equals(IConsts.ATTRMULTI))
				w.setMulti(bv);
			else if (k.equals(IConsts.ATTRSELECTED))
				w.setSelected(v);
			else if (k.equals(IConsts.ATTRSELECTEDATTRIBUTE))
				w.setSelectedAttribute(v);
			else if (k.equals(IConsts.ATTRSELECTEDITEM))
				w.setSelectedItem(v);
			else if (k.equals(IConsts.ATTRSELECTEDITEMS))
				w.setSelectedItems(v);
			else if (k.equals(IConsts.ATTRSELECTEDVALUES))
				w.setSelectedValues(v);
			else if (k.equals(IConsts.ATTRALIGNBOTTOM))
				w.setAlignBottom(bv);
			else if (k.equals(IConsts.ATTRDISABLEDRAG))
				w.setDisableDrag(bv);
			else if (k.equals(IConsts.ATTRHIDESCROLLBUTTONS))
				w.setHideScrollButtons(bv);
			else if (k.equals(IConsts.ATTRNOBAR))
				w.setNoBar(bv);
			else if (k.equals(IConsts.ATTRNOSLIDE))
				w.setNoSlide(bv);

		}
	};

	private static final IVisitor<PaperFab> paperfabG = new IVisitor<PaperFab>() {

		@Override
		public void visit(PaperFab w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRPOINTERDOWN))
				w.setPointerDown(bv);
			else if (k.equals(IConsts.ATTRPRESSED))
				w.setPressed(bv);
			else if (k.equals(IConsts.ATTRTOGGLES))
				w.setToggles(bv);
			else if (k.equals(IConsts.ATTRACTIVE))
				w.setActive(bv);
			else if (k.equals(IConsts.ATTRELEVATION))
				w.setElevation(v);
			else if (k.equals(IConsts.ATTRARIAACTIVEATTRIBUTE))
				w.setAriaActiveAttribute(v);
			else if (k.equals(IConsts.ATTRELEVATIONFLOAT))
				w.setElevation(dv);
			else if (k.equals(IConsts.ATTRSTOPKEYBORADFROMPROPAGATION))
				w.setStopKeyboardEventPropagation(bv);
			else if (k.equals(IConsts.ATTRICON))
				w.setIcon(v);
			else if (k.equals(IConsts.ATTRSRC))
				w.setSrc(v);
			else if (k.equals(IConsts.ATTRMINI))
				w.setMini(bv);
			else if (k.equals(IConsts.ATTRKEYBINDINGS))
				w.setKeyBindings(v);
		}
	};

	private static final IVisitor<PaperInput> paperinputG = new IVisitor<PaperInput>() {

		@Override
		public void visit(PaperInput w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRACCEPT))
				w.setAccept(v);
			else if (k.equals(IConsts.ATTRALLOWEDPATTERN))
				w.setAllowedPattern(v);
			else if (k.equals(IConsts.ATTRALWAYSFLOATLABEL))
				w.setAlwaysFloatLabel(bv);
			else if (k.equals(IConsts.ATTRAUTOCAPITALIZE))
				w.setAutocapitalize(v);
			else if (k.equals(IConsts.ATTRAUTOCOMPLETE))
				w.setAutocomplete(v);
			else if (k.equals(IConsts.ATTRAUTOCORRECT))
				w.setAutocorrect(v);
			else if (k.equals(IConsts.ATTRAUTOFOCUS))
				w.setAutofocus(bv);
			else if (k.equals(IConsts.ATTRAUTOSAVE))
				w.setAutosave(v);
			else if (k.equals(IConsts.ATTRAUTOVALIDATE))
				w.setAutoValidate(bv);
			else if (k.equals(IConsts.ATTRCHARCOUNTER))
				w.setCharCounter(bv);
			else if (k.equals(IConsts.ATTRERRORMESSAGE))
				w.setErrorMessage(v);
			else if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRINPUTMODE))
				w.setInputmode(v);
			else if (k.equals(IConsts.ATTRINVALID))
				w.setInvalid(bv);
			else if (k.equals(IConsts.ATTRLABEL))
				w.setLabel(v);
			else if (k.equals(IConsts.ATTRLIST))
				w.setList(v);
			else if (k.equals(IConsts.ATTRMAX))
				w.setMax(v);
			else if (k.equals(IConsts.ATTRMAXLENGTH))
				w.setMaxlength(dv);
			else if (k.equals(IConsts.ATTRMAXLENGTHS))
				w.setMaxlength(v);
			else if (k.equals(IConsts.ATTRMIN))
				w.setMin(v);
			else if (k.equals(IConsts.ATTRMINLENGTH))
				w.setMinlength(dv);
			else if (k.equals(IConsts.ATTRMINLENGTHS))
				w.setMinlength(v);
			else if (k.equals(IConsts.ATTRNOLABELFLOAT))
				w.setNoLabelFloat(bv);
			else if (k.equals(IConsts.ATTRPATTERN))
				w.setPattern(v);
			else if (k.equals(IConsts.ATTRPLACEHOLDER))
				w.setPlaceholder(v);
			else if (k.equals(IConsts.ATTRPREVENTINVALIDINPUT))
				w.setPreventInvalidInput(bv);
			else if (k.equals(IConsts.ATTRREADONLY))
				w.setReadonly(bv);
			else if (k.equals(IConsts.ATTRREQUIRED))
				w.setRequired(bv);
			else if (k.equals(IConsts.ATTRRESULS))
				w.setResults(dv);
			else if (k.equals(IConsts.ATTRRESULSS))
				w.setResults(v);
			else if (k.equals(IConsts.ATTRSIZES))
				w.setSize(v);
			else if (k.equals(IConsts.ATTRTYPE))
				w.setType(v);
			else if (k.equals(IConsts.ATTRVALIDATOR))
				w.setValidator(v);
			else if (k.equals(IConsts.ATTRVALUE))
				w.setValue(v);
		}
	};

	private static final IVisitor<PaperTextarea> papertextareaG = new IVisitor<PaperTextarea>() {

		@Override
		public void visit(PaperTextarea w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRACCEPT))
				w.setAccept(v);
			else if (k.equals(IConsts.ATTRALLOWEDPATTERN))
				w.setAllowedPattern(v);
			else if (k.equals(IConsts.ATTRALWAYSFLOATLABEL))
				w.setAlwaysFloatLabel(bv);
			else if (k.equals(IConsts.ATTRAUTOCAPITALIZE))
				w.setAutocapitalize(v);
			else if (k.equals(IConsts.ATTRAUTOCOMPLETE))
				w.setAutocomplete(v);
			else if (k.equals(IConsts.ATTRAUTOCORRECT))
				w.setAutocorrect(v);
			else if (k.equals(IConsts.ATTRAUTOFOCUS))
				w.setAutofocus(bv);
			else if (k.equals(IConsts.ATTRAUTOSAVE))
				w.setAutosave(v);
			else if (k.equals(IConsts.ATTRAUTOVALIDATE))
				w.setAutoValidate(bv);
			else if (k.equals(IConsts.ATTRCHARCOUNTER))
				w.setCharCounter(bv);
			else if (k.equals(IConsts.ATTRERRORMESSAGE))
				w.setErrorMessage(v);
			else if (k.equals(IConsts.ATTRFOCUSED))
				w.setFocused(bv);
			else if (k.equals(IConsts.ATTRINPUTMODE))
				w.setInputmode(v);
			else if (k.equals(IConsts.ATTRINVALID))
				w.setInvalid(bv);
			else if (k.equals(IConsts.ATTRLABEL))
				w.setLabel(v);
			else if (k.equals(IConsts.ATTRLIST))
				w.setList(v);
			else if (k.equals(IConsts.ATTRMAX))
				w.setMax(v);
			else if (k.equals(IConsts.ATTRMAXLENGTH))
				w.setMaxlength(dv);
			else if (k.equals(IConsts.ATTRMAXLENGTHS))
				w.setMaxlength(v);
			else if (k.equals(IConsts.ATTRMIN))
				w.setMin(v);
			else if (k.equals(IConsts.ATTRMINLENGTH))
				w.setMinlength(dv);
			else if (k.equals(IConsts.ATTRMINLENGTHS))
				w.setMinlength(v);
			else if (k.equals(IConsts.ATTRNOLABELFLOAT))
				w.setNoLabelFloat(bv);
			else if (k.equals(IConsts.ATTRPATTERN))
				w.setPattern(v);
			else if (k.equals(IConsts.ATTRPLACEHOLDER))
				w.setPlaceholder(v);
			else if (k.equals(IConsts.ATTRPREVENTINVALIDINPUT))
				w.setPreventInvalidInput(bv);
			else if (k.equals(IConsts.ATTRREADONLY))
				w.setReadonly(bv);
			else if (k.equals(IConsts.ATTRREQUIRED))
				w.setRequired(bv);
			else if (k.equals(IConsts.ATTRRESULS))
				w.setResults(dv);
			else if (k.equals(IConsts.ATTRRESULSS))
				w.setResults(v);
			else if (k.equals(IConsts.ATTRSIZES))
				w.setSize(v);
			else if (k.equals(IConsts.ATTRTYPE))
				w.setType(v);
			else if (k.equals(IConsts.ATTRVALIDATOR))
				w.setValidator(v);
			else if (k.equals(IConsts.ATTRVALUE))
				w.setValue(v);
			else if (k.equals(IConsts.ATTRMAXROWS))
				w.setMaxRows(dv);
			else if (k.equals(IConsts.ATTRMAXROWSS))
				w.setMaxRows(v);
			else if (k.equals(IConsts.ATTRMULTIPLE))
				w.setMultiple(bv);
			else if (k.equals(IConsts.ATTRROWS))
				w.setRows(dv);
			else if (k.equals(IConsts.ATTRROWSS))
				w.setRows(v);
		}
	};

	private static final IVisitor<PaperMaterial> papermaterialG = new IVisitor<PaperMaterial>() {

		@Override
		public void visit(PaperMaterial w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRELEVATION))
				w.setElevation(v);
			else if (k.equals(IConsts.ATTRELEVATIONFLOAT))
				w.setElevation(dv);
			else if (k.equals(IConsts.ATTRANIMATED))
				w.setAnimated(bv);
		}
	};

	private static final IVisitor<PaperProgress> paperprogressG = new IVisitor<PaperProgress>() {

		@Override
		public void visit(PaperProgress w, String k, String v, boolean bv, double dv) {
			if (k.equals(IConsts.ATTRMAX))
				w.setMax(dv);
			else if (k.equals(IConsts.ATTRMAXS))
				w.setMax(v);
			else if (k.equals(IConsts.ATTRMIN))
				w.setMin(dv);
			else if (k.equals(IConsts.ATTRMINS))
				w.setMin(v);
			else if (k.equals(IConsts.ATTRRATIO))
				w.setRatio(dv);
			else if (k.equals(IConsts.ATTRRATIOS))
				w.setRatio(v);
			else if (k.equals(IConsts.ATTRSECONDARYPROGESS))
				w.setSecondaryProgress(dv);
			else if (k.equals(IConsts.ATTRSECONDARYPROGESSS))
				w.setSecondaryProgress(v);
			else if (k.equals(IConsts.ATTRSECONDARYRATIO))
				w.setSecondaryRatio(dv);
			else if (k.equals(IConsts.ATTRSECONDARYRATIOS))
				w.setSecondaryRatio(v);
			else if (k.equals(IConsts.ATTRSTEP))
				w.setStep(dv);
			else if (k.equals(IConsts.ATTRSTEPS))
				w.setStep(v);
			else if (k.equals(IConsts.ATTRVALUE))
				w.setValue(dv);
			else if (k.equals(IConsts.ATTRVALUES))
				w.setValue(v);
			else if (k.equals(IConsts.ATTRINDETERMINATE))
				w.setIndeterminate(bv);
		}

	};

	private final static Map<WidgetTypes, IVisitor<Widget>[]> setAWidget = new HashMap<WidgetTypes, IVisitor<Widget>[]>();

	static {
		setAWidget.put(WidgetTypes.Button, new IVisitor[] { argF, argbaseB });
		setAWidget.put(WidgetTypes.Label, new IVisitor[] { argL });
		setAWidget.put(WidgetTypes.HTMLPanel, new IVisitor[] {});
		setAWidget.put(WidgetTypes.PaperIconItem, new IVisitor[] { polymerWidgetG, paperIconItemG });
		setAWidget.put(WidgetTypes.IronIcon, new IVisitor[] { polymerWidgetG, ironIconG });
		setAWidget.put(WidgetTypes.PaperButton, new IVisitor[] { polymerWidgetG, paperbuttonG });
		setAWidget.put(WidgetTypes.PaperHeaderPanel, new IVisitor[] { polymerWidgetG, paperHeaderG });
		setAWidget.put(WidgetTypes.PaperToolbar, new IVisitor[] { polymerWidgetG, paperToolbarG });
		setAWidget.put(WidgetTypes.Image, new IVisitor[] { imageG });
		setAWidget.put(WidgetTypes.PaperIconButton, new IVisitor[] { polymerWidgetG, papericonG });
		setAWidget.put(WidgetTypes.PaperDrawerPanel, new IVisitor[] { polymerWidgetG, paperdrawerpanelG });
		setAWidget.put(WidgetTypes.PaperCheckbox, new IVisitor[] { polymerWidgetG, papercheckboxG });
		setAWidget.put(WidgetTypes.PaperDialog, new IVisitor[] { polymerWidgetG, paperdialogG });
		setAWidget.put(WidgetTypes.PaperDialogScrollable, new IVisitor[] { polymerWidgetG, paperdialogscrollableG });
		setAWidget.put(WidgetTypes.PaperDropdownMenu, new IVisitor[] { polymerWidgetG, paperdropdownmenuG });
		setAWidget.put(WidgetTypes.PaperMenu, new IVisitor[] { polymerWidgetG, papermenuG });
		setAWidget.put(WidgetTypes.PaperTabs, new IVisitor[] { polymerWidgetG, papertabsG });
		setAWidget.put(WidgetTypes.PaperFab, new IVisitor[] { polymerWidgetG, paperfabG });
		setAWidget.put(WidgetTypes.PaperItem, new IVisitor[] { polymerWidgetG, paperItemG });
		setAWidget.put(WidgetTypes.PaperItemBody, new IVisitor[] { polymerWidgetG });
		setAWidget.put(WidgetTypes.PaperInput, new IVisitor[] { polymerWidgetG, paperinputG });
		setAWidget.put(WidgetTypes.PaperTextarea, new IVisitor[] { polymerWidgetG, papertextareaG });
		setAWidget.put(WidgetTypes.PaperMaterial, new IVisitor[] { polymerWidgetG, papermaterialG });
		setAWidget.put(WidgetTypes.PaperProgress, new IVisitor[] { polymerWidgetG, paperprogressG });
	}

	private WidgetTypes widgetToType(Widget w) {
		String typeName = w.getClass().getSimpleName();
		try {
			return WidgetTypes.valueOf(typeName);
		} catch (IllegalArgumentException e) {
			Utils.errAlertB(typeName, LogT.getT().WidgetToSetAttrNotImplemented());
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setAttr(Widget w, String attr, String val) {
		String v = PolymerUtil.convert(val);
		boolean bval = false;
		double dval = -1;
		if (!CUtil.EmptyS(v)) {
			bval = Utils.toB(v);
			// exception expected
			dval = DecimalUtils.toDoubleE(v, -1);
		}
		argW.visit(w, attr, v, bval, dval);
		for (IVisitor vi : setAWidget.get(widgetToType(w)))
			vi.visit(w, attr, v, bval, dval);
	}

}
