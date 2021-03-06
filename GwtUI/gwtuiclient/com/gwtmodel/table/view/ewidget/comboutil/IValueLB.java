/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.ewidget.comboutil;

import java.util.List;

import com.gwtmodel.table.TOptional;
import com.gwtmodel.table.editw.IFormLineView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IValueLB extends IFormLineView {

    void setList(List<String> li);

    void setIdList(List<String> li);

    TOptional<String> getBeforeVal();

    boolean addEmpty();
}
