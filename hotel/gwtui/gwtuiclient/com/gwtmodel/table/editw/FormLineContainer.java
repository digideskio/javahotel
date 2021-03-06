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
package com.gwtmodel.table.editw;

import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import java.util.ArrayList;
import java.util.List;

public class FormLineContainer {

	private final List<FormField> fList;
	private final boolean polymer;

	public FormLineContainer(List<FormField> fList, boolean polymer) {
		this.fList = fList;
		this.polymer = polymer;
	}

	public FormField findFormField(IVField v) {
		for (FormField f : fList) {
			if (f.getFie().eq(v)) {
				return f;
			}
		}
		return null;
	}

	public IFormLineView findLineView(IVField v) {
		FormField f = findFormField(v);
		if (f == null) {
			return null;
		}
		return f.getELine();
	}

	/**
	 * Get list of IGetSetVField related for FormContainer
	 *
	 * @return List
	 */
	public List<IGetSetVField> getvList() {
		List<IGetSetVField> vList = new ArrayList<IGetSetVField>();
		for (FormField f : fList) {
			vList.add(f.getELine());
		}
		return vList;
	}

	public List<FormField> getfList() {
		return fList;
	}

	public void addFormField(FormField f) {
		fList.add(f);
	}

	public boolean isPolymer() {
		return polymer;
	}

}
