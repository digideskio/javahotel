/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.mvc.apanel;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GwtPanel implements IPanel {

	private final Panel pa;

	public GwtPanel(Panel pa) {
		this.pa = pa;

	}

	public void add(Widget w) {
		pa.add(w);
	}

	public Widget getPanel() {
		return pa;
	}

}
