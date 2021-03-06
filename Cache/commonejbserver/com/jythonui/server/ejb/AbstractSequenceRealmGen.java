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
package com.jythonui.server.ejb;

import com.jythonui.server.storage.seq.ISequenceRealmGen;

abstract class AbstractSequenceRealmGen implements ISequenceRealmGen {
    
    protected ISequenceRealmGen iSeq;

    @Override
    public Long genNext(String realM, String key) {
        return iSeq.genNext(realM, key);
    }

    @Override
    public void remove(String realm, String key) {
        iSeq.remove(realm, key);        
    }


}
