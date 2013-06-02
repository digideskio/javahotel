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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel",
        "service", "room" }))
@NamedQueries({
    @NamedQuery(name = "findServicesForRoom", query = "SELECT x FROM EHotelRoomServices x WHERE x.hotel = ?1 AND x.room.name=?2"),
    @NamedQuery(name = "deleteServicesForRoom", query = "DELETE FROM EHotelRoomServices x WHERE x.hotel = ?1 AND x.room=?2"),
    @NamedQuery(name = "deleteAllRoomServices", query = "DELETE FROM EHotelRoomServices x WHERE x.hotel = ?1"),
    @NamedQuery(name = "deleteForRoomServices", query = "DELETE FROM EHotelRoomServices x WHERE x.hotel = ?1 AND x.service = ?2") })
public class EHotelRoomServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hotel;

    @JoinColumn(name = "service_id", nullable = false)
    private EHotelServices service;

    @JoinColumn(name = "room_id", nullable = false)
    private EHotelRoom room;

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public EHotelServices getService() {
        return service;
    }

    public void setService(EHotelServices service) {
        this.service = service;
    }

    public EHotelRoom getRoom() {
        return room;
    }

    public void setRoom(EHotelRoom room) {
        this.room = room;
    }

}
