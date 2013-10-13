/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.prices.HotelPriceElem;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;

public class Test8 extends TestHelper {

    private final static String SERVICE1 = "1p1";
    private final static String PRICE1 = "price1";

    @Before
    public void clearData() {
        clearObjects();
        createHotels();
    }

    private void addStartD() {
        HotelServices ho = new HotelServices();
        ho.setName(SERVICE1);
        ho.setDescription("One person in one person room");
        ho.setNoPersons(2);
        ho.setAttr(IHotelConsts.VATPROP, "7%");
        iServices.addElem(getH(HOTEL), ho);

        HotelPriceList p = new HotelPriceList();
        p.setName(PRICE1);
        p.setFromDate(toDate(2010, 10, 1));
        iPrice.addElem(getH(HOTEL), p);

    }

    @Test
    public void test1() {
        addStartD();
        List<HotelPriceElem> pList = iPriceElem.getPricesForPriceList(
                getH(HOTEL), PRICE1);
        assertTrue(pList.isEmpty());
        HotelPriceElem eElem = new HotelPriceElem();
        eElem.setService(SERVICE1);
        eElem.setWeekendPrice(new BigDecimal(10.0));
        eElem.setWorkingPrice(new BigDecimal(20.0));
        pList = new ArrayList<HotelPriceElem>();
        pList.add(eElem);
        iPriceElem.savePricesForPriceList(getH(HOTEL), PRICE1, pList);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        assertEquals(1, pList.size());
        eElem = pList.get(0);
        assertEqB(10.0,eElem.getWeekendPrice());
        assertEqB(20.0,eElem.getWorkingPrice());
        eElem.setWorkingPrice(new BigDecimal(30.0));
        pList = new ArrayList<HotelPriceElem>();
        pList.add(eElem);
        iPriceElem.savePricesForPriceList(getH(HOTEL), PRICE1, pList);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        eElem = pList.get(0);
        assertEquals(new BigDecimal(10),eElem.getWeekendPrice());
        assertEquals(new BigDecimal(30),eElem.getWorkingPrice());
    }

    @Test
    public void test2() {
        addStartD();
        List<HotelPriceElem> pList = iPriceElem.getPricesForPriceList(
                getH(HOTEL), PRICE1);
        HotelPriceElem eElem = new HotelPriceElem();
        eElem.setService(SERVICE1);
        eElem.setWeekendPrice(new BigDecimal(10.0));
        eElem.setWorkingPrice(new BigDecimal(20.0));
        pList = new ArrayList<HotelPriceElem>();
        pList.add(eElem);
        iPriceElem.savePricesForPriceList(getH(HOTEL), PRICE1, pList);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        assertEquals(1, pList.size());
        // remove pricelist
        HotelPriceList p = new HotelPriceList();
        p.setName(PRICE1);
        iPrice.deleteElem(getH(HOTEL), p);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        assertTrue(pList.isEmpty());
    }

    @Test
    public void test3() {
        addStartD();
        List<HotelPriceElem> pList = iPriceElem.getPricesForPriceList(
                getH(HOTEL), PRICE1);
        HotelPriceElem eElem = new HotelPriceElem();
        eElem.setService(SERVICE1);
        eElem.setWeekendPrice(new BigDecimal(10.0));
        eElem.setWorkingPrice(new BigDecimal(20.0));
        pList = new ArrayList<HotelPriceElem>();
        pList.add(eElem);
        iPriceElem.savePricesForPriceList(getH(HOTEL), PRICE1, pList);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        assertEquals(1, pList.size());
        // remove pricelist
        HotelServices ho = new HotelServices();
        ho.setName(SERVICE1);
        iServices.deleteElem(getH(HOTEL), ho);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        assertTrue(pList.isEmpty());
    }

    @Test
    public void test4() {
        addStartD();
        for (int i = 0; i < 100; i++) {
            HotelServices ho = new HotelServices();
            ho.setName("s" + i);
            ho.setDescription("One person in one person room");
            ho.setNoPersons(2);
            ho.setAttr(IHotelConsts.VATPROP, "7%");
            iServices.addElem(getH(HOTEL), ho);
        }
        List<HotelPriceElem> pList = new ArrayList<HotelPriceElem>();
        for (int i = 0; i < 90; i++) {
            HotelPriceElem eElem = new HotelPriceElem();
            eElem.setService("s" + i);
            eElem.setWeekendPrice(new BigDecimal(10.0 + i));
            eElem.setWorkingPrice(new BigDecimal(20.0 + i));
            pList.add(eElem);
        }
        iPriceElem.savePricesForPriceList(getH(HOTEL), PRICE1, pList);

        pList = new ArrayList<HotelPriceElem>();
        for (int i = 80; i < 100; i++) {
            HotelPriceElem eElem = new HotelPriceElem();
            eElem.setService("s" + i);
            eElem.setWeekendPrice(new BigDecimal(30.0 + i));
            eElem.setWorkingPrice(new BigDecimal(40.0 + i));
            pList.add(eElem);
        }
        iPriceElem.savePricesForPriceList(getH(HOTEL), PRICE1, pList);

        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        // assertEquals(100, pList.size());
        // 2013/06/04 : should be 20
        assertEquals(20, pList.size());
        boolean found = false;
        for (HotelPriceElem e : pList) {
            if (e.getService().equals("s81")) {
                assertEquals(e.getWeekendPrice(), new BigDecimal(111.0));
                assertEquals(e.getWorkingPrice(), new BigDecimal(121.0));
                found = true;
            }
        }
        assertTrue(found);
        HotelServices ho = new HotelServices();
        ho.setName("s81");
        iServices.deleteElem(getH(HOTEL), ho);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        // assertEquals(99, pList.size());
        // 2013/06/04 : should be 19
        assertEquals(19, pList.size());
        HotelPriceList p = new HotelPriceList();
        p.setName(PRICE1);
        iPrice.deleteElem(getH(HOTEL), p);
        pList = iPriceElem.getPricesForPriceList(getH(HOTEL), PRICE1);
        assertTrue(pList.isEmpty());

    }

}
