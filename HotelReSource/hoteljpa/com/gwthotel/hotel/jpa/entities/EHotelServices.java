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
package com.gwthotel.hotel.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gwthotel.hotel.ServiceType;
import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "findAllServices", query = "SELECT x FROM EHotelServices x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deleteAllServices", query = "DELETE FROM EHotelServices x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneService", query = "SELECT x FROM EHotelServices x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelServices extends EObjectDict {

    @Column(nullable = false)
    private String vat;

    @Column(nullable = false)
    private int noPersons;

    @Column(nullable = false)
    private ServiceType serviceType;
    
    @Column(nullable = false)
    private boolean perperson;
    
    @Column(nullable = false)
    private int noExtraBeds;
    
    @Column(nullable = false)
    private int noChildren;
    

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public int getNoPersons() {
        return noPersons;
    }

    public void setNoPersons(int noPersons) {
        this.noPersons = noPersons;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isPerperson() {
        return perperson;
    }

    public void setPerperson(boolean perperson) {
        this.perperson = perperson;
    }

    public int getNoExtraBeds() {
        return noExtraBeds;
    }

    public void setNoExtraBeds(int noExtraBeds) {
        this.noExtraBeds = noExtraBeds;
    }

    public int getNoChildren() {
        return noChildren;
    }

    public void setNoChildren(int noChildren) {
        this.noChildren = noChildren;
    }
    
    

}
