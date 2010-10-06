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
package com.javahotel.client.widgets.solid;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class SolidColor extends Composite {

    private final HTML d;

    public SolidColor(final String color, final int width, final int height) {
        String sx = "<div style=\"background-color:";
        sx += color;
        sx += ";width:";
        sx += width;
        sx += "px\">&nbsp;</div>";
        d = new HTML(sx);
        initWidget(d);
    }

    public void addClickListener(final ClickListener c) {
        d.addClickListener(c);
    }
}
