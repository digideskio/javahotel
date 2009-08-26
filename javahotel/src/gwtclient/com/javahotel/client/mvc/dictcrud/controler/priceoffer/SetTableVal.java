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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SetTableVal {

	private SetTableVal() {
	}

	private static void setWid(int i, ArrayList<BigDecimal> val, BigDecimal b) {
		val.set(i, b);
	}

	private static class S implements ISpecialMap {

		private final IDecimalTableView tView;
		private final OfferPriceP oP;

		S(IDecimalTableView tView, OfferPriceP oP) {
			this.tView = tView;
			this.oP = oP;
		}

		public void set(final ArrayList<MapSpecialToI> col) {
			ArrayList<String> rows = tView.getSRow();
			if (rows == null) {
				return;
			}
			for (int r = 0; r < rows.size(); r++) {
				String ss = rows.get(r);
				ArrayList<BigDecimal> va = new ArrayList<BigDecimal>();
				int si = ISeasonPriceModel.MAXSPECIALNO + col.size() + 1;
				for (int i = 0; i < si; i++) {
					va.add(null);
				}
				Collection<OfferServicePriceP> colO = oP.getServiceprice();
				if (colO != null) {
					for (OfferServicePriceP pe : colO) {
						if (pe.getService().equals(ss)) {
							setWid(ISeasonPriceModel.HIGHSEASON, va, pe
									.getHighseasonprice());
							setWid(ISeasonPriceModel.HIGHSEASONWEEKEND, va, pe
									.getHighseasonweekendprice());
							setWid(ISeasonPriceModel.LOWSEASON, va, pe
									.getLowseasonprice());
							setWid(ISeasonPriceModel.LOWSEASONWEEKEND, va, pe
									.getLowseasonweekendprice());
							for (int ii = 0; ii < col.size(); ii++) {
								Long pid = col.get(ii).getSpecId();
								for (OfferSpecialPriceP oo : pe
										.getSpecialprice()) {
									if (pid.equals(oo.getSpecialperiod())) {
										setWid(ISeasonPriceModel.MAXSPECIALNO
												+ ii + 1, va, oo.getPrice());
									}
								}
							}
						}
					}
				}
				tView.setRowVal(r, va);
			}
		}
	}

	static void setVal(IResLocator rI, GetSeasonSpecial sS,
			IDecimalTableView tView, OfferPriceP oP) {
		sS.runSpecial(oP.getSeason(), new S(tView, oP));
	}
}
