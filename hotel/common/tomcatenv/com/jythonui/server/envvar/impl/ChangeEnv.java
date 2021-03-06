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
package com.jythonui.server.envvar.impl;

import com.jythonui.server.IConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class ChangeEnv extends UtilHelper {

	private ChangeEnv() {

	}

	public static String replaceV(String v) {
		if (v.length() > 1 && v.charAt(0) == IConsts.ENVVARIABLE) {
			String val = System.getenv(v.substring(1));
			if (val == null) {
				// ENVIRONMENTVARIABLENODEFINED
				errorLog(L().getMess(IErrorCode.ERRORCODE141, ILogMess.ENVIRONMENTVARIABLENODEFINED, v.substring(1)));
				return null;
			}
			return val;
		} else
			return v;
	}

}
