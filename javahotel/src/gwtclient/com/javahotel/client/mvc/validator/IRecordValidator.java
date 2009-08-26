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

package com.javahotel.client.mvc.validator;

import com.javahotel.client.mvc.crud.controler.RecordModel;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface IRecordValidator {

	void setErrContext(IErrorMessageContext iCo);

	void validateS(int action, RecordModel a, ISignalValidate sig);

	boolean isEmpty(RecordModel a);
}
