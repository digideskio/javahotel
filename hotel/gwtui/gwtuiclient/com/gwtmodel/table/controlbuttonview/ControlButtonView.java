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
package com.gwtmodel.table.controlbuttonview;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.menudef.MenuPullContainer;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.panelview.BinderWidgetSignal;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ButtonAction;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.stackpanelcontroller.IStackPanelController;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.pullmenu.PullMenuFactory;

class ControlButtonView extends AbstractSlotContainer implements IControlButtonView, IStackPanelController {

	private final IContrButtonView vButton;
	private final Map<ClickButtonType, ClickButtonType> redirMap = new HashMap<ClickButtonType, ClickButtonType>();
	private final Map<ClickButtonType, ButtonRedirectSignal> redirRMap = new HashMap<ClickButtonType, ButtonRedirectSignal>();
	private boolean redirActivated = false;

	private class Click implements IControlClick {

		@Override
		public void click(ControlButtonDesc co, Widget w) {
			if (redirActivated) {
				ButtonRedirectSignal bRedir = redirRMap.get(co.getActionId());
				if (bRedir != null) {
					// TODO: not nice, fixing reference kept locally
					// better to make a clone and set attribute in the clone
					bRedir.setW(new GWidget(w));
					publish(bRedir.getSl(), bRedir);
					return;
				}
			}
			ClickButtonType bType = redirMap.get(co.getActionId());
			publish(bType != null ? bType : co.getActionId(), new GWidget(w));
		}
	}

	private class EnableButton implements ISlotListener {

		private final ButtonAction bAction;
		private final ClickButtonType actionId;

		EnableButton(ButtonAction bAction, ClickButtonType actionId) {
			this.actionId = actionId;
			this.bAction = bAction;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			LogT.getLS().info(LogT.getT().receivedSignalLog(slContext.getSlType().toString()));
			vButton.setEnable(actionId, bAction.getAction() == ButtonAction.Action.EnableButton);
		}
	}

	private class HideButton implements ISlotListener {

		private final ButtonAction bAction;
		private final ClickButtonType actionId;

		HideButton(ButtonAction bAction, ClickButtonType actionId) {
			this.actionId = actionId;
			this.bAction = bAction;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			LogT.getLS().info(LogT.getT().receivedSignalLog(slContext.getSlType().toString()));
			vButton.setHidden(actionId, bAction.getAction() == ButtonAction.Action.HideButton);
		}
	}

	private class RedirectButton implements ISlotListener {

		private final ControlButtonDesc b;

		RedirectButton(ControlButtonDesc b) {
			this.b = b;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			redirMap.put(b.getActionId(), slContext.getSlType().getbAction().getRedirectB());
		}
	}

	/**
	 * Listener for 'force button' action
	 * 
	 * @author hotel
	 * 
	 */
	private class ForceButton implements ISlotListener {

		private final ControlButtonDesc b;

		ForceButton(ControlButtonDesc b) {
			this.b = b;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			vButton.emulateClick(b.getActionId());

		}
	}

	private class RedirectRButton implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			ICustomObject c = slContext.getCustom();
			ButtonRedirectSignal bu = (ButtonRedirectSignal) c;
			redirRMap.put(bu.getbType(), bu);
		}
	}

	private class RedirectActivateSignal implements ISlotListener {

		@Override
		public void signal(ISlotSignalContext slContext) {
			ICustomObject c = slContext.getCustom();
			ButtonRedirectActivateSignal bu = (ButtonRedirectActivateSignal) c;
			redirActivated = bu.getValue();
		}
	}

	private void register(ButtonAction.Action bA, ControlButtonDesc b) {
		ButtonAction ba = new ButtonAction(bA);
		EnableButton e = new EnableButton(ba, b.getActionId());
		registerSubscriber(dType, b.getActionId(), ba, e);
	}

	private void registerhide(ButtonAction.Action bA, ControlButtonDesc b) {
		ButtonAction ba = new ButtonAction(bA);
		HideButton e = new HideButton(ba, b.getActionId());
		registerSubscriber(dType, b.getActionId(), ba, e);
	}

	private class GetterContainer implements ISlotCallerListener {

		@Override
		public ISlotSignalContext call(ISlotSignalContext slContext) {
			GetButtons bu = new GetButtons(vButton.construct());
			return slContextFactory.construct(slContext.getSlType(), bu);
		}
	}

	ControlButtonView(IContrButtonViewFactory vFactory, ListOfControlDesc listButton, IDataType dType, boolean hori,
			boolean polymer) {
		this.dType = dType;
		this.vButton = vFactory.getView(listButton, new Click(), hori, polymer);
		for (ControlButtonDesc b : listButton.getcList()) {
			register(ButtonAction.Action.EnableButton, b);
			register(ButtonAction.Action.DisableButton, b);
			registerhide(ButtonAction.Action.HideButton, b);
			registerhide(ButtonAction.Action.ShowButton, b);
			ButtonAction bR = new ButtonAction(ButtonAction.Action.RedirectButton);
			registerSubscriber(dType, b.getActionId(), bR, new RedirectButton(b));
			ButtonAction fR = new ButtonAction(ButtonAction.Action.ForceButton);
			registerSubscriber(dType, b.getActionId(), fR, new ForceButton(b));
		}
		// one general listener
		CustomStringSlot sl = ButtonRedirectSignal.constructSlotButtonRedirectSignal(dType);
		registerSubscriber(sl, new RedirectRButton());
		sl = ButtonRedirectActivateSignal.constructSlotButtonRedirectActivateSignal(dType);
		registerSubscriber(sl, new RedirectActivateSignal());
		registerCaller(GetButtons.constructSlot(dType), new GetterContainer());

	}

	ControlButtonView(PullMenuFactory menuFactory, MenuPullContainer menu, IDataType dType) {
		this.dType = dType;
		vButton = menuFactory.construct(menu, new Click());
	}

	@Override
	public void startPublish(CellId cellId) {
		BinderWidgetSignal bw = SlU.getBW(dType, this);
		if (bw != null) {
			vButton.fillHtml(bw.getValue(),bw.getBw());
			return;
		}
		IGWidget w = getHtmlWidget(cellId);
		if (w != null) {
			vButton.setHtml(w);
		}
		publish(dType, cellId, vButton);
	}
}
