/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.defa;

import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import com.jython.ui.shared.ISharedConsts;
import com.jython.ui.shared.MUtil;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.RequestContext;

public class GetClientProperties implements IJythonClientRes {

    @SuppressWarnings("unused")
    static final private Logger log = Logger
            .getLogger(GetClientProperties.class.getName());
    @SuppressWarnings("unused")
    private final IGetLogMess gMess;
    private final IGetAppProp iApp;

    @Inject
    public GetClientProperties(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            IGetAppProp iApp) {
        this.gMess = gMess;
        this.iApp = iApp;
    }

    @Override
    public ClientProp getClientRes(RequestContext context) {
        Holder.setContext(context);
        Map<String, String> prop = iApp.get();
        ClientProp map = new ClientProp();
        map.setMap(prop);
        Holder.setAuth(map.isAuthenticate());
        IAppMess custMess = Holder.getAppMess();
        CustomMessages mess = custMess.getCustomMess();
        if (mess != null)
            map.setCustomM(mess);
        return map;
    }
}
