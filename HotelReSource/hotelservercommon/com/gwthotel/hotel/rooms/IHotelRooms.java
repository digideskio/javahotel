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
package com.gwthotel.hotel.rooms;

import java.util.List;

import com.gwthotel.hotel.services.HotelServices;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.crud.IObjectCrud;

public interface IHotelRooms extends IObjectCrud<HotelRoom> {

    void setRoomServices(OObjectId hotel, String roomName, List<String> services);

    List<HotelServices> getRoomServices(OObjectId hotel, String roomName);

}