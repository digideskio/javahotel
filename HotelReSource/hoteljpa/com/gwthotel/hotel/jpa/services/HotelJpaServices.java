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
package com.gwthotel.hotel.jpa.services;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.jpa.HotelAbstractJpaCrud;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;

public class HotelJpaServices extends
        HotelAbstractJpaCrud<HotelServices, EHotelServices> implements
        IHotelServices {

    @Inject
    public HotelJpaServices(ITransactionContextFactory eFactory,
            IJpaObjectGenSymFactory iGen) {
        super(new String[] { "findAllServices", "findOneService" }, eFactory,
                HotelObjects.SERVICE, iGen, EHotelServices.class);
    }

    @Override
    protected HotelServices toT(EHotelServices sou, EntityManager em,
            OObjectId hotel) {
        return JUtils.toT(sou);
    }

    @Override
    protected void toE(EHotelServices dest, HotelServices sou,
            EntityManager em, OObjectId hotel) {
        dest.setNoPersons(sou.getNoPersons());
        dest.setVat(sou.getAttr(IHotelConsts.VATPROP));
        dest.setServiceType(sou.getServiceType());
        dest.setNoChildren(sou.getNoChildren());
        dest.setNoExtraBeds(sou.getNoExtraBeds());
        dest.setPerperson(sou.isPerperson());
    }

    @Override
    protected void beforedeleteElem(EntityManager em, OObjectId hotel,
            EHotelServices elem) {
        String qList[] = { "deleteAllReservationDetailsForService",
                "deletePricesForHotelAndService", "deleteForRoomServices" };
        JUtils.runQueryForObject(em, elem, qList);
    }

    @Override
    protected void afterAddChange(EntityManager em, OObjectId hotel,
            HotelServices prop, EHotelServices elem, boolean add) {

    }
}
