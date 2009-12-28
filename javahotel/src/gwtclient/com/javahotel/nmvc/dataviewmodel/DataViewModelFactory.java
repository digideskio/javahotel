/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.dataviewmodel;

import com.javahotel.common.toobject.AbstractTo;

public class DataViewModelFactory {

    private class DataViewModel implements IDataViewModel {

        public void pumpValues(AbstractTo aTo) {
            // TODO Auto-generated method stub

        }

        public void suckValues(AbstractTo aTo) {
            // TODO Auto-generated method stub

        }

    }

    public IDataViewModel construct() {
        return new DataViewModel();
    }

}