/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.pricemodel;

import java.math.BigDecimal;
import java.util.List;

import com.javahotel.common.toobject.OfferPriceP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface ISeasonPriceModel {

    int HIGHSEASON = 0;
    int HIGHSEASONWEEKEND = 1;
    int LOWSEASON = 2;
    int LOWSEASONWEEKEND = 3;
    int MAXSPECIALNO = 3;

    List<String> pricesNames();
    
    int noPrices();

    List<BigDecimal> getPrices(OfferPriceP priceP, String service);

    void setPrices(OfferPriceP priceP, String service, List<BigDecimal> prices);

}
