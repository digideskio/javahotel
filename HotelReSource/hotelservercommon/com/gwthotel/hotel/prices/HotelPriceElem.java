/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.prices;

import java.math.BigDecimal;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.shared.PropDescription;

public class HotelPriceElem extends PropDescription {

    private static final long serialVersionUID = 1L;
    private BigDecimal price;
    private BigDecimal childrenPrice;
    private BigDecimal extrabedsPrice;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getChildrenPrice() {
        return childrenPrice;
    }

    public void setChildrenPrice(BigDecimal childrenPrice) {
        this.childrenPrice = childrenPrice;
    }

    public BigDecimal getExtrabedsPrice() {
        return extrabedsPrice;
    }

    public void setExtrabedsPrice(BigDecimal extrabedsPrice) {
        this.extrabedsPrice = extrabedsPrice;
    }

    public void setService(String service) {
        setAttr(IHotelConsts.PRICELISTSERVICEPROP, service);
    }

    public String getService() {
        return getAttr(IHotelConsts.PRICELISTSERVICEPROP);
    }

    public void setPriceList(String pricelist) {
        setAttr(IHotelConsts.PRICELISTPRICEPROP, pricelist);
    }

    public String getPriceList() {
        return getAttr(IHotelConsts.PRICELISTPRICEPROP);
    }

}
