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
package com.jythonui.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jythonui.client.IJythonUIClientResources;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class JResource implements IJythonUIClientResources {

    private final JythonServiceAsync jythonService = GWT
            .create(JythonService.class);

    @Override
    public void getDialogFormat(String name,
            AsyncCallback<DialogFormat> callback) {
        jythonService.getDialogFormat(name, callback);

    }

    @Override
    public void runAction(DialogVariables v, String name, String actionId,
            AsyncCallback<DialogVariables> callback) {
        jythonService.runAction(v, name, actionId, callback);
    }

}
