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
package com.gwtmodel.table.slotmodel;

public class ValidateActionType {

    public enum ValidateActionEnum {

        /** Sent to validate date. */
        Validate,

        /** Sent if error. */
        ValidatonFailed,

        /** Sent if success. */
        ValidationPassed,

    };

    public enum ValidateType {
        ADD, MODIF, REMOVE, NOTHING
    };

    private final ValidateActionEnum vAction;
    private final ValidateType vType;

    public ValidateActionType(ValidateActionEnum vAction) {
        this.vAction = vAction;
        this.vType = ValidateType.NOTHING;
    }

    public ValidateActionType(ValidateActionEnum vAction, ValidateType vType) {
        this.vAction = vAction;
        this.vType = vType;
    }

    public ValidateActionEnum getvAction() {
        return vAction;
    }

    public ValidateType getvType() {
        return vType;
    }

    boolean eq(ValidateActionType v) {
        if (vAction != v.vAction) {
            return false;
        }
        return vType == v.vType;
    }
}