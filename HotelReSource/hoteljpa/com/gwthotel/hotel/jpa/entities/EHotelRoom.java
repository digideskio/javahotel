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
package com.gwthotel.hotel.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllRooms", query = "SELECT x FROM EHotelRoom x WHERE x.hotel = ?1"),
    @NamedQuery(name = "deleteAllRooms", query = "DELETE FROM EHotelRoom x WHERE x.hotel = ?1"),
    @NamedQuery(name = "findOneRoom", query = "SELECT x FROM EHotelRoom x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelRoom extends EHotelDict  {

    @Column(nullable = false)
    private int noPersons;
    
    public int getNoPersons() {
        return noPersons;
    }

    public void setNoPersons(int noPersons) {
        this.noPersons = noPersons;
    }
}
