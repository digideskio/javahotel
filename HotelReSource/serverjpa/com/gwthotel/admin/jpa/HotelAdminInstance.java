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
package com.gwthotel.admin.jpa;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.jpa.entities.EHotel;
import com.gwthotel.admin.jpa.entities.EInstance;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

class HotelAdminInstance implements IAppInstanceHotel {

    private final EntityManagerFactory eFactory;
    private static final Logger log = Logger.getLogger(HotelAdminInstance.class
            .getName());
    private final IGetLogMess lMess;

    HotelAdminInstance(EntityManagerFactory eFactory, IGetLogMess lMess) {
        this.eFactory = eFactory;
        this.lMess = lMess;
    }

    private abstract class doTransaction extends JpaTransaction {

        private doTransaction() {
            super(eFactory, lMess);
        }
    }

    private class getInstance extends doTransaction {

        private final String instanceName;
        Long id;

        getInstance(String instanceName) {
            this.instanceName = instanceName;
        }

        @Override
        protected void dosth(EntityManager em) {
            EInstance insta = null;
            Query q = em.createNamedQuery("findInstanceByName");
            q.setParameter(1, instanceName);
            try {
                insta = (EInstance) q.getSingleResult();
            } catch (NoResultException e) {
                // test if default instances
                if (instanceName.equals(IHotelConsts.INSTANCETEST)
                        || instanceName.equals(IHotelConsts.INSTANCEDEFAULT)) {
                    insta = new EInstance();
                    insta.setName(instanceName);
                    em.persist(insta);
                    commitandbegin(em);
                    log.info(lMess.getMessN(
                            IHMess.DEFAULTINSTANCEHASBEENCREATED, instanceName));
                } else
                    throw (e);
            }
            id = insta.getId();
            if (id == null) {
                String mess = lMess.getMess(IHError.HERROR011,
                        IHMess.INSTANCEIDCANNOTNENULLHERE);
                log.severe(mess);
                throw new JythonUIFatal(mess);
            }

        }
    }

    @Override
    public AppInstanceId getInstanceId(String instanceName) {
        getInstance comm = new getInstance(instanceName);
        comm.executeTran();
        AppInstanceId id = new AppInstanceId();
        id.setId(comm.id);
        id.setInstanceName(instanceName);
        return id;
    }

    private class getHotelId extends doTransaction {

        private final AppInstanceId instanceId;
        private final String hotelName;
        Long id;

        getHotelId(AppInstanceId instanceId, String hotelName) {
            this.instanceId = instanceId;
            this.hotelName = hotelName;
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findHotelByName");
            q.setParameter(1, instanceId.getId());
            q.setParameter(2, hotelName);
            EHotel h = (EHotel) q.getSingleResult();
            // do not catch exception, not expected here
            id = h.getId();
        }

    }

    @Override
    public HotelId getHotelId(AppInstanceId instanceId, String hotelName) {
        getHotelId comm = new getHotelId(instanceId, hotelName);
        comm.executeTran();
        HotelId h = new HotelId();
        h.setHotel(hotelName);
        h.setId(comm.id);
        return h;
    }

}
