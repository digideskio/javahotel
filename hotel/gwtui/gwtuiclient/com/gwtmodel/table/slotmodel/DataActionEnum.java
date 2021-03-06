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
package com.gwtmodel.table.slotmodel;

public enum DataActionEnum {

    /** Draw list / DataListType. */
    DrawListAction,
    /** Refresh only cell data. */
    RefreshListAction,
    /** Set filter */
    DrawListSetFilter,
    /** Remove filter */
    DrawListRemoveFilter,
    /** find item */
    FindRowList,
    /** find next item */
    FindRowNextList,
    /** find from beginning */
    FindRowBeginningList,
    /** Signal not found */
    NotFoundSignal,
    /** Change view mode / PersistTypeEnum . */
    ChangeViewFormModeAction,
    /** Change view mode / PersistTypeEnum . */
    ChangeViewComposeFormModeAction,
    /** Change view mode to invalid state / InvalidateFormContainer. */
    ChangeViewFormToInvalidAction,
    /** ClearForm action. */
    ClearViewFormAction,
    /** Draw form. / IVModeData. */
    DrawViewFormAction,
    /** Draw form. / IVModeData. */
    DrawViewComposeFormAction,
    /** Draw form. / IVModeData. */
    DefaultViewFormAction,
    /** Draw form. / IVModeData. */
    DefaultViewComposeFormAction,
    /** ValidateActionSignal / IVModelData / PersistTypeEnum. */
    ValidateAction,
    /** InvalidSignal / InvalidateFormContainer / PersistEnumType. */
    InvalidSignal,
    /** ValidSignal / PersistEnumType . */
    ValidSignal,
    /** Read list / IDataListType. */
    ReadListAction,
    /** List read successfully. */
    ReadListPersistedSignal,
    /** Read header / VListHeaderContainer . */
    ReadHeaderContainer,
    /** Header read successfully. */
    ReadHeaderContainerSignal,
    /** Persist data / IVModelData / PersistEnumType. */
    PersistDataAction,
    /** Persist data successful. */
    PersistDataSuccessSignal,
    /** List read successfully. / DataListType */
    ListReadSuccessSignal,
    /** Data persisted successfully / PersisEnumType . */
    DataPersistedSuccessSignal,
    /** RefreshAfterPersistSignal / PersistEnumType. */
    RefreshAfterPersistActionSignal,
    /** Validate compose form / PersistEnumType . */
    ValidateComposeFormAction,
    /** Compose form valid . */
    ValidComposeFormSuccessSignal,
    /** Persist compose form / PersistEnumType. */
    PersistComposeFormAction,
    /** Persist action successful / PersistEnumType */
    PersistComposeFormSuccessSignal,
    /** Line clicked / IVModelData + WSIze */
    TableLineClicked,
    /** Action cell clicked. */
    TableCellClicked,
    /** After FormDialog is displayed. */
    AfterDrawViewFormAction,
    /** After ChangeMode is executed. */
    AfterChangeModeFormAction,
    /** Draw footer. */
    DrawFooterAction,
    /** Get list size. */
    GetListSize
}
