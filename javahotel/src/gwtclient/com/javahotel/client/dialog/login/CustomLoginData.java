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
package com.javahotel.client.dialog.login;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.login.LoginField;
import com.javahotel.common.util.StringU;

class CustomLoginData extends LoginData {

    public String getHotel() {
        return hotel;
    }

    private String hotel;

    @Override
    public String getS(IVField fie) {
        LoginField f = (LoginField) fie;
        if (f.getF() == LoginField.F.OTHER) {
            return hotel;
        }
        return super.getS(fie);
    }

    @Override
    public boolean isEmpty(IVField fie) {
        LoginField f = (LoginField) fie;
        if (f.getF() == LoginField.F.OTHER) {
            return StringU.isEmpty(hotel);
        }
        return super.isEmpty(fie);
    }

    @Override
    public void setF(IVField fie, Object val) {
        LoginField f = (LoginField) fie;
        if (f.getF() == LoginField.F.OTHER) {
            hotel = (String) val;
            return;
        }
        super.setF(fie, val);
    }
}