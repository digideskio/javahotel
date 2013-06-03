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
package com.gwthotel.hotel.jpa.rooms;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelRoomServices;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.HotelServices;
import com.jythonui.server.getmess.IGetLogMess;

class HotelJpaRooms extends AbstractJpaCrud<HotelRoom, EHotelRoom> implements
        IHotelRooms {

    HotelJpaRooms(EntityManagerFactory eFactory, IGetLogMess lMess) {
        super(new String[] { "findAllRooms", "findOneRoom", "deleteAllRooms" },
                eFactory, lMess);
    }

    @Override
    protected HotelRoom toT(EHotelRoom sou) {
        HotelRoom ho = new HotelRoom();
        ho.setNoPersons(sou.getNoPersons());
        return ho;
    }

    @Override
    protected EHotelRoom constructE() {
        return new EHotelRoom();
    }

    @Override
    protected void toE(EHotelRoom dest, HotelRoom sou) {
        dest.setNoPersons(sou.getNoPersons());
    }

    @Override
    protected void beforedeleteAll(EntityManager em, String hotel) {
        Query q = em.createNamedQuery("deleteAllRoomServices");
        q.setParameter(1, hotel);
        q.executeUpdate();
    }

    @Override
    protected void beforedeleteElem(EntityManager em, String hotel,
            EHotelRoom elem) {
        Query q = em.createNamedQuery("deleteServicesForRoom");
        q.setParameter(1, hotel);
        q.setParameter(2, elem);
        q.executeUpdate();
    }

    private class SetRoomServices extends doTransaction {

        private final String roomName;
        private final List<String> services;

        SetRoomServices(String hotel, String roomName, List<String> services) {
            super(hotel);
            this.roomName = roomName;
            this.services = services;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelRoom r = getElem(em, roomName);
            // TODO: more descriptive log in null
            Query q = em.createNamedQuery("deleteServicesForRoom");
            q.setParameter(1, hotel);
            q.setParameter(2, r);
            q.executeUpdate();
            for (String s : services) {
                q = em.createNamedQuery("findOneService");
                q.setParameter(1, hotel);
                q.setParameter(2, s);
                EHotelServices se = (EHotelServices) q.getSingleResult();
                // do not catch exception
                EHotelRoomServices eServ = new EHotelRoomServices();
                eServ.setHotel(hotel);
                eServ.setRoom(r);
                eServ.setService(se);
                em.persist(eServ);
            }
        }

    }

    @Override
    public void setRoomServices(String hotel, String roomName,
            List<String> services) {
        SetRoomServices comma = new SetRoomServices(hotel, roomName, services);
        comma.executeTran();

    }

    private class GetRoomServices extends doTransaction {
        private final String roomName;
        private final List<HotelServices> eList = new ArrayList<HotelServices>();

        GetRoomServices(String hotel, String roomName) {
            super(hotel);
            this.roomName = roomName;
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findServicesForRoom");
            q.setParameter(1, hotel);
            q.setParameter(2, roomName);
            List<EHotelRoomServices> rList = q.getResultList();
            for (EHotelRoomServices r : rList) {
                eList.add(JUtils.toT(r.getService()));
            }
        }
    }

    @Override
    public List<HotelServices> getRoomServices(String hotel, String roomName) {
        GetRoomServices comm = new GetRoomServices(hotel, roomName);
        comm.executeTran();
        return comm.eList;
    }
}
