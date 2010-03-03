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
package com.gwtmodel.table.injector;

import com.gwtmodel.table.view.webpanel.IWebPanel;

public class WebPanelHolder {
    
    private static IWebPanel iWeb;
    private static boolean googletable;

    /**
     * @return the googletable
     */
    public static boolean isGoogletable() {
        return googletable;
    }

    /**
     * @param aGoogletable the googletable to set
     */
    public static void setGoogletable(boolean aGoogletable) {
        googletable = aGoogletable;
    }
    
    private WebPanelHolder() {
        
    }
    
    public static void setWebPanel(IWebPanel i) {
        iWeb = i;
    }
    
    static IWebPanel getWebPanel() {
        return iWeb;
    }

}