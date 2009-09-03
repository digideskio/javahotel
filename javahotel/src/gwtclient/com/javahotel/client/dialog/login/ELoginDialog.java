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
package com.javahotel.client.dialog.login;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ICommand;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.DictValidatorFactory;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordDefFactory;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.RecordViewFactory;
import com.javahotel.client.mvc.recordviewdef.DictButtonFactory;
import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ELoginDialog extends Composite {

    private final IResLocator rI;
    private final ICommand iNext;
    private final IRecordView v;
    private final DictData da;

    private class ValidBack implements ISignalValidate {

        public void success() {
            iNext.execute();
        }

        public void failue(IErrorMessage errmess) {
            v.showInvalidate(errmess);
        }
    }

    private class LoginClick implements IControlClick {

        public void click(ContrButton co, Widget w) {
            LoginRecord re = new LoginRecord();
            RecordModel mo = new RecordModel(null, null);
            mo.setRDef(v.getModel());
            mo.setA(re);
            v.extractFields(mo);
            IRecordValidator val = DictValidatorFactory.getValidator(rI, da);
            val.validateS(0, mo, new ValidBack());
        }
    }

    public ELoginDialog(final IResLocator rI, final boolean user,
            final ICommand iNext) {
        this.rI = rI;
        this.iNext = iNext;
        IContrPanel co = DictButtonFactory.getLoginButt(rI);
        da = new DictData(user ? SpecE.LoginUser : SpecE.LoginAdmin);
        ArrayList<RecordField> field = GetRecordDefFactory.getDef(rI, da);
        String dTitle = GetRecordDefFactory.getTitle(rI, da);
        IRecordDef model = RecordDefFactory.getRecordDef(rI, dTitle, field);
        v = RecordViewFactory.getRecordView(rI, da, model, co,
                new LoginClick(), null);
        initWidget(v.getMWidget().getWidget());
    }
}
