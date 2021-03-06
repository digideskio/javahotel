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
package com.gwtmodel.table.controler;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.controler.DataListActionItemFactory.DrawForm;
import com.gwtmodel.table.controler.DataListActionItemFactory.ResignAction;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.editw.FormField;
import com.gwtmodel.table.editw.FormFieldPropFactory;
import com.gwtmodel.table.editw.FormLineContainer;
import com.gwtmodel.table.editw.IFormChangeListener;
import com.gwtmodel.table.editw.IFormLineView;
import com.gwtmodel.table.editw.ITouchListener;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.listdataview.IsBooleanSignalNow;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.mm.MM;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.gwtmodel.table.view.helper.HelperDialogFactory;
import com.gwtmodel.table.view.helper.IStandDialog;
import com.gwtmodel.table.view.util.CreateReadOnly;
import com.gwtmodel.table.view.util.FormUtil;
import com.gwtmodel.table.view.webpanel.IWebPanel;

/**
 * 
 * @author perseus
 */
class FindListActionFactory {

	private final GetHeader gHeader = new GetHeader();
	private final TablesFactories tFactories;
	private final IDataType ddType;
	private final IDataType eType;
	private final DataViewModelFactory vFactory;
	private final DataListParam listParam;
	private List<FormField> liFilter = null;
	private List<FormField> liFind = null;

	FindListActionFactory(TablesFactories tFactories, IDataType dType, DataListParam listParam) {
		this.tFactories = tFactories;
		this.ddType = dType;
		eType = Empty.getDataType();
		vFactory = tFactories.getdViewFactory();
		this.listParam = listParam;
	}

	private class GetHeader implements ISlotListener {

		private VListHeaderContainer listHeader = null;

		@Override
		public void signal(ISlotSignalContext slContext) {
			listHeader = slContext.getListHeader();
		}
	}

	private class RemoveFilter implements ISlotListener {

		private final DrawForm dForm;
		private final ISlotable publishSlo;
		private final IDataType publishdType;

		RemoveFilter(DrawForm dForm, ISlotable publishSlo, IDataType publishdType) {
			this.dForm = dForm;
			this.publishSlo = publishSlo;
			this.publishdType = publishdType;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			publishSlo.getSlContainer().publish(publishdType, DataActionEnum.DrawListRemoveFilter);
			dForm.hide();
		}
	}

	private class NotFoundSignal implements ISlotListener {

		private IGWidget w;

		@Override
		public void signal(ISlotSignalContext slContext) {
			IStandDialog ok = HelperDialogFactory.construct(MM.getL().NotFound());
			if (w == null) {
				Utils.errAlert("signal", LogT.getT().notFoundSignalNotNull());
			}
			ok.show(new WSize(w));
		}

		/**
		 * @param w
		 *            the w to set
		 */
		public void setW(IGWidget w) {
			this.w = w;
			if (w == null) {
				Utils.errAlert("setW", LogT.getT().notFoundSignalNotNull());
			}
		}
	}

	private class ClearParam implements ISlotListener {

		private final ISlotMediator slMediator;
		private final List<FormField> liF;
		private final List<VListHeaderDesc> li;
		private final NotFoundSignal nF;

		ClearParam(ISlotMediator slMediator, List<FormField> liF, List<VListHeaderDesc> li, NotFoundSignal nF) {
			this.slMediator = slMediator;
			this.liF = liF;
			this.li = li;
			this.nF = nF;
		}

		public void signal(ISlotSignalContext slContext) {
			FData fa = returnNotEmpty(slMediator, liF, li, slContext, nF);
			if (fa == null) {
				return;
			}
			IClickYesNo yes = new IClickYesNo() {
				public void click(boolean yes) {
					if (yes) {
						slMediator.getSlContainer().publish(eType, DataActionEnum.ClearViewFormAction);
						clearBoolean(liF);
					}
				}
			};
			// YesNoDialog yesDialog = new
			// YesNoDialog(MM.getL().ClearParametersQuestion(), yes);
			// yesDialog.show(nF.w.getGWidget());
			HelperDialogFactory.constructyes(MM.getL().ClearParametersQuestion(), null, yes)
					.show(new WSize(nF.w.getGWidget()));
		}
	}

	private FData returnNotEmpty(ISlotMediator slMediator, List<FormField> liF, List<VListHeaderDesc> li,
			ISlotSignalContext slContext, NotFoundSignal nF) {
		FData fa = new FData(liF, li);
		slMediator.getSlContainer().getGetterIVModelData(eType, GetActionEnum.GetViewModelEdited, fa);
		IGWidget w = slContext.getGwtWidget();
		nF.setW(w);
		if (fa.isEmpty()) {
			IStandDialog ok = HelperDialogFactory.construct(MM.getL().NothingEntered());
			ok.show(new WSize(w));
			return null;
		}
		return fa;
	}

	private class SetFilter implements ISlotListener {

		private final ISlotMediator slMediator;
		private final List<FormField> liF;
		private final DrawForm dForm;
		private final List<VListHeaderDesc> li;
		private final ISlotable publishSlo;
		private final IDataType publishdType;
		private final DataActionEnum a;
		private final boolean hide;
		private final NotFoundSignal nF;

		SetFilter(ISlotMediator slMediator, List<FormField> liF, DrawForm dForm, List<VListHeaderDesc> li,
				ISlotable publishSlo, IDataType publishdType, DataActionEnum a, boolean hide, NotFoundSignal nF) {
			this.slMediator = slMediator;
			this.liF = liF;
			this.dForm = dForm;
			this.li = li;
			this.publishSlo = publishSlo;
			this.publishdType = publishdType;
			this.a = a;
			this.hide = hide;
			this.nF = nF;
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			FData fa = returnNotEmpty(slMediator, liF, li, slContext, nF);
			if (fa != null) {
				publishSlo.getSlContainer().publish(publishdType, a, fa.constructIOk());
			} else {
				return; // do not close the dialog if empty
			}
			if (hide) {
				dForm.hide();
			}
		}
	}

	private void modifLogField(IFormLineView iFrom, IFormLineView iignore) {
		iFrom.setReadOnly(false);
		if (iignore == null) {
			return;
		}
		Boolean b = (Boolean) iignore.getValObj();
		if (!b.booleanValue()) {
			return;
		}
		iFrom.setReadOnly(true);
	}

	private void clearBoolean(List<FormField> li) {
		for (FormField f : li) {
			IVField fie = f.getFie();
			FField fe = (FField) fie;
			if (fe.isCheckField()) {
				continue;
			}
			if (fe.isIgnoreField()) {
				Boolean val = new Boolean(true);
				f.getELine().setValObj(val);
				FField fCheck = FField.constructFrom(fe.getV());
				FormField formCheck = FormUtil.findFormField(li, fCheck);
				val = new Boolean(false);
				formCheck.getELine().setValObj(val);
				modifLogField(formCheck.getELine(), f.getELine());
				continue;
			}
			if (fie.getType().getType() == TT.BOOLEAN) {
				// handled above
				continue;
			}
		}

	}

	private void modifCheck(IVModelData v, IVField from, IVField to, IFormLineView icheck) {
		boolean nullFrom = FUtils.isNullValue(v, from);
		boolean nullTo = FUtils.isNullValue(v, to);
		if (nullFrom || !nullTo) {
			icheck.setReadOnly(true);
			return;
		}
		icheck.setReadOnly(false);
	}

	private class Touch implements ITouchListener {

		private final IVField from;
		private final IVField to;
		private final IFormLineView icheck;
		private final List<FormField> liF;

		@Override
		public void onTouch() {
			FormLineContainer fContainer = new FormLineContainer(liF, false);
			IVModelData v = CreateReadOnly.contructReadonlyVModel(fContainer);
			modifCheck(v, from, to, icheck);
		}

		/**
		 * @param from
		 * @param to
		 * @param icheck
		 * @param liF
		 */
		Touch(IVField from, IVField to, IFormLineView icheck, List<FormField> liF) {
			this.from = from;
			this.to = to;
			this.icheck = icheck;
			this.liF = liF;
		}
	}

	private class OnLogChange implements IFormChangeListener {

		private final IFormLineView iFrom;

		OnLogChange(IFormLineView iFrom) {
			this.iFrom = iFrom;
		}

		@Override
		public void onChange(IFormLineView i, boolean afterFocus) {
			modifLogField(iFrom, i);
		}

	}

	private List<FormField> constructForm(List<VListHeaderDesc> li) {

		List<FormField> liF = new ArrayList<FormField>();
		// cannot use Set - use List instead
		List<IVField> vLi = new ArrayList<IVField>();
		for (VListHeaderDesc he : li) {
			boolean found = false;
			for (IVField v : vLi) {
				if (v.eq(he.getFie())) {
					found = true;
					break;
				}
			}
			if (found)
				continue;
			IVField fe = he.getFie();
			vLi.add(fe);
			IVField from = FField.constructFrom(he);
			IVField to = FField.constructTo(he);
			IVField check = FField.constructCheck(he);
			IVField ignore = FField.constructIgnore(he);
			IFormLineView icheck = null;
			if (!fe.getType().isBoolean()) {
				icheck = EditWidgetFactory.getGwtE().constructCheckField(check,
						FormFieldPropFactory.constructDisplayName(MM.getL().EqualSign()));
				icheck.setValObj(new Boolean(true));
				liF.add(new FormField(null, icheck, check, from));
			}
			IFormLineView ifrom = EditWidgetFactory.constructEditWidget(from, null);
			// modify htmlId for 'to' field
			String htmlToName = to.getId() + "-1";
			IFormLineView ito = EditWidgetFactory.constructEditWidget(to, FormFieldPropFactory.construct(htmlToName));
			liF.add(new FormField(he.getHeaderString(), ifrom));
			ITouchListener t = null;
			if (icheck != null) {
				liF.add(new FormField(null, icheck, check, from));
				t = new Touch(from, to, icheck, liF);
				ifrom.setOnTouch(t);
				ito.setOnTouch(t);
			}
			if (!fe.getType().isBoolean()) {
				liF.add(new FormField(null, ito, to, from));
			} else {
				icheck = EditWidgetFactory.getGwtE().constructCheckField(ignore,
						FormFieldPropFactory.constructDisplayName(MM.getL().IngnoreDuringSearch()));
				liF.add(new FormField(null, icheck, ignore, from));
				icheck.addChangeListener(new OnLogChange(ifrom));
			}
			// important: after adding (not before)
			if (t != null)
				t.onTouch();

		}
		clearBoolean(liF);
		return liF;
	}

	private class ActionFind implements ISlotListener {

		private final ClickButtonType.StandClickEnum action;
		private ISlotMediator slMediator;
		private final ISlotable publishSlo;
		private final IDataType publishdType;
		private final NotFoundSignal nF;

		private boolean isFilter() {
			return action == ClickButtonType.StandClickEnum.FILTRLIST;
		}

		ActionFind(ClickButtonType.StandClickEnum action, ISlotable publishSlo, IDataType publishdType,
				NotFoundSignal nF) {
			this.action = action;
			this.publishSlo = publishSlo;
			this.publishdType = publishdType;
			this.nF = nF;
			this.slMediator = null;
		}

		private class Re implements ISignal {

			@Override
			public void signal() {
				publishSlo.getSlContainer().removeSubscriber(publishdType, DataActionEnum.NotFoundSignal);
				slMediator = null;
			}
		}

		@Override
		public void signal(ISlotSignalContext slContext) {
			if (gHeader.listHeader == null) {
				return;
			}
			if (!isFilter() && slMediator != null) {
				return;
			}
			IGWidget wi = slContext.getGwtWidget();
			WSize wSize = new WSize(wi.getGWidget());
			if (!isFilter() && TUtil.isBoolProp(publishSlo, IsBooleanSignalNow.constructSlotAsyncProvider(ddType))) {
				HelperDialogFactory.construct(MM.getL().CannotRunFindInAsycnMode()).show(wSize);
				return;
			}

			final ISignal remF = new Re();
			List<VListHeaderDesc> li = gHeader.listHeader.getVisHeList();
			List<FormField> liF;
			boolean modal;

			if (isFilter()) {
				if (liFilter == null) {
					liFilter = constructForm(li);
				}
				liF = liFilter;
				modal = true;
			} else {
				if (liFind == null) {
					liFind = constructForm(li);
				}
				liF = liFind;
				modal = false;
			}

			FormLineContainer fContainer = new FormLineContainer(liF, false);
			// 2012/03/30 : parameter added
			IDataViewModel daView = vFactory.construct(eType, fContainer, listParam.getDataFactory());
			ListOfControlDesc bControl;
			if (isFilter()) {
				bControl = tFactories.getControlButtonFactory().constructFilterButton();
			} else {
				bControl = tFactories.getControlButtonFactory().constructFindButton();
			}

			IControlButtonView bView = ControlButtonViewFactory.construct(eType, bControl, false);
			CellId panelId = new CellId(1);
			IPanelView pView = PanelViewFactory.construct(eType, panelId);
			CellId controlId = pView.addCellPanel(1, 0);
			CellId cellTableId = pView.addCellPanel(0, 0);
			pView.createView();
			slMediator = tFactories.getSlotMediatorFactory().construct();
			slMediator.registerSlotContainer(panelId, pView);
			slMediator.registerSlotContainer(cellTableId, daView);
			slMediator.registerSlotContainer(controlId, bView);

			ICallContext iCall = GwtGiniInjector.getI().getCallContext();
			iCall.setdType(ddType);
			iCall.setiSlo(publishSlo);
			iCall.setPersistTypeEnum(slContext.getPersistType());

			String title = listParam.getFormFactory().getFormTitle(iCall);
			ISignal o = new ISignal() {
				@Override
				public void signal() {
					remF.signal();
				}
			};
			final DrawForm dForm = new DrawForm(wSize, title, listParam.getFormFactory().getSolidPos(iCall), action,
					modal, o, null);
			ISlotListener clearS = new ClearParam(slMediator, liF, li, nF);
			slMediator.getSlContainer().registerSubscriber(eType, panelId, dForm);
			ResignAction aRes = new ResignAction(dForm, remF, null);
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.RESIGN, aRes);

			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.SETFILTER,
					new SetFilter(slMediator, liF, dForm, li, publishSlo, publishdType,
							DataActionEnum.DrawListSetFilter, true, nF));
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.FINDNOW, new SetFilter(
					slMediator, liF, dForm, li, publishSlo, publishdType, DataActionEnum.FindRowList, false, nF));
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.FINDFROMBEGINNING,
					new SetFilter(slMediator, liF, dForm, li, publishSlo, publishdType,
							DataActionEnum.FindRowBeginningList, false, nF));
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.FINDNEXT,
					new SetFilter(slMediator, liF, dForm, li, publishSlo, publishdType, DataActionEnum.FindRowNextList,
							false, nF));
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.REMOVEFILTER,
					new RemoveFilter(dForm, publishSlo, publishdType));
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.CLEARFILTER, clearS);
			slMediator.getSlContainer().registerSubscriber(eType, ClickButtonType.StandClickEnum.CLEARFIND, clearS);
			if (!isFilter()) {
				publishSlo.getSlContainer().registerSubscriber(publishdType, DataActionEnum.NotFoundSignal, nF);
			}

			slMediator.startPublish(panelId);
			if (!isFilter()) {
				IWebPanel wPanel = GwtGiniInjector.getI().getWebPanel();
				if (wPanel != null) {
					ISignal iSig = new ISignal() {
						@Override
						public void signal() {
							dForm.hide();
						}
					};
					wPanel.setCentreHideSignal(iSig);
				}
			}
		}
	}

	ISlotListener constructActionFind(ClickButtonType.StandClickEnum eNum, ISlotable publishSlo,
			IDataType publishdType) {
		return new ActionFind(eNum, publishSlo, publishdType, new NotFoundSignal());
	}

	ISlotListener constructActionHeader() {
		return gHeader;
	}
}
