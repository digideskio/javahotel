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
package com.gwthotel.hotel.guice;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.hotel.IHotelObjectsFactory;
import com.gwthotel.hotel.objectfactoryimpl.HotelObjectsFactory;
import com.gwthotel.hotel.server.service.H;
import com.gwthotel.mess.HotelMessProvider;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.objectauth.SecurityConverter;
import com.jythonui.server.security.ISecurityConvert;

public class HotelCommonGuice {

    public abstract static class HotelServiceModule extends JythonServiceModule {

        protected void configureHotel() {
            configureJythonUi();
            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(IHotelConsts.MESSNAMED))
                    .toProvider(HotelMessProvider.class).in(Singleton.class);
            bind(ISecurityConvert.class).to(SecurityConverter.class).in(
                    Singleton.class);
            bind(IHotelObjectsFactory.class).to(HotelObjectsFactory.class).in(
                    Singleton.class);
            requestStaticInjection(HHolder.class);
            requestStaticInjection(H.class);
        }

    }
}