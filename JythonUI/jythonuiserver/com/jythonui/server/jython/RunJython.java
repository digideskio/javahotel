/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.jython;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyIterator;
import org.python.core.PyList;
import org.python.core.PyLong;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.MCached;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DateLineVariables;
import com.jythonui.shared.DialogCheckVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ElemDescription;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;
import com.jythonui.shared.TypedefDescr;
import com.jythonui.shared.TypesDescr;

/**
 * @author hotel
 * 
 *         Interface between Java code and Jython Call Jython actions and
 *         pass/gets back client data
 */
public class RunJython {

    private final static String GGTempVariable = "GG";
    private final static String AATempVariable = "AA";

    /** Logger. */
    static final private Logger log = Logger.getLogger(RunJython.class
            .getName());

    /**
     * Put debug message to logger
     * 
     * @param mess
     *            Debug message
     */
    static private void putDebug(String mess) {
        log.log(Level.FINE, mess);
    }

    /**
     * Writes error message and throws unchecked exception
     * 
     * @param mess
     *            Fatal error message
     */
    static private void error(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    /** Constant Jython string. */
    private static final PyObject JLISTMAP = toString(ICommonConsts.JLISTMAP);
    private static final PyObject JCHECKMAP = toString(ICommonConsts.JCHECKLISTMAP);
    private static final PyObject JDATELINEMAP = toString(ICommonConsts.JDATELINEMAP);

    private static Map<PyObject, PyObject> toPythonMap(DialogVariables v) {
        Map<PyObject, PyObject> m = new HashMap<PyObject, PyObject>();
        for (String s : v.getFields()) {
            FieldValue val = v.getValue(s);
            PyObject valP = null;
            if (val.getValue() == null) {
                valP = Py.None;
            } else
                switch (val.getType()) {
                case STRING:
                    String valS = val.getValueS();
                    if (valS == null)
                        error(Holder.getM().getMess(IErrorCode.ERRORCODE36,
                                ILogMess.STRINGVALUECANNOTBENULL, s));

                    valP = new PyString(valS);
                    break;
                case BOOLEAN:
                    Boolean b = val.getValueB();
                    if (b == null) {
                        error(Holder.getM().getMess(IErrorCode.ERRORCODE37,
                                ILogMess.BOOLEANVALUECANNOTBENULL, s));
                    }
                    valP = new PyBoolean(b.booleanValue());
                    break;
                case INT:
                    Integer i = val.getValueI();
                    valP = new PyInteger(i);
                    break;
                case LONG:
                    Long l = val.getValueL();
                    valP = new PyLong(l);
                    break;
                case BIGDECIMAL:
                    BigDecimal bi = val.getValueBD();
                    valP = new PyFloat(bi.doubleValue());
                    break;
                case DATE:
                    java.sql.Date dt = new java.sql.Date(val.getValueD()
                            .getTime());
                    PyObject da = Py.newDate(dt);
                    valP = da;
                    break;
                case DATETIME:
                    PyObject ti = Py.newDatetime(val.getValueT());
                    valP = ti;
                    break;
                default:
                    error(Holder.getM().getMess(IErrorCode.ERRORCODE33,
                            ILogMess.TYPEMAPNOTIMPLEMENTS, s,
                            val.getType().toString()));
                    break;
                }
            m.put(toString(s), valP);

        }
        return m;
    }

    private static DialogVariables toVariables(RowIndex rI, RowContent row) {
        DialogVariables v = new DialogVariables();
        for (int i = 0; i < rI.rowSize(); i++) {
            FieldItem f = rI.getI(i);
            v.setValue(f.getId(), row.getRow(i));
        }
        return v;
    }

    private static PyObject toString(String s) {
        return new PyString(s);
    }

    private static PyList createList(List<FieldItem> cList, ListOfRows rList) {
        Collection<PyDictionary> c = new ArrayList<PyDictionary>();
        RowIndex rI = new RowIndex(cList);
        for (RowContent row : rList.getRowList()) {
            DialogVariables var = toVariables(rI, row);
            Map<PyObject, PyObject> ma = toPythonMap(var);
            PyDictionary pMa = new PyDictionary(ma);
            c.add(pMa);
        }
        PyList pList = new PyList(c);
        return pList;
    }

    private static void addListToMap(Map<PyObject, PyObject> pMap,
            DialogFormat d, DialogVariables v) {
        Map<String, ListOfRows> rowList = v.getRowList();
        if (rowList.isEmpty()) {
            return;
        }
        Map<PyObject, PyObject> m = new HashMap<PyObject, PyObject>();
        Iterator<Entry<String, ListOfRows>> iter = rowList.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Entry<String, ListOfRows> e = iter.next();
            ListFormat fList = d.findList(e.getKey());
            if (fList == null)
                error(Holder.getM().getMess(IErrorCode.ERRORCODE34, d.getId(),
                        ILogMess.LISTNOTFOUND, e.getKey()));
            PyList pList = createList(fList.getColumns(), e.getValue());
            m.put(toString(e.getKey()), pList);
        }
        PyDictionary pyMap = new PyDictionary(m);
        pMap.put(JLISTMAP, pyMap);
    }

    private static void addCheckListToMap(Map<PyObject, PyObject> pMap,
            DialogFormat d, DialogVariables v) {
        if (v.getCheckVariables().isEmpty())
            return;
        Map<PyObject, PyObject> pyMap = new HashMap<PyObject, PyObject>();
        for (String checkS : v.getCheckVariables().keySet()) {
            Map<PyObject, PyObject> m = new HashMap<PyObject, PyObject>();
            DialogCheckVariables var = v.getCheckVariables().get(checkS);
            CheckList cList = DialogFormat.findE(d.getCheckList(), checkS);
            for (String s : var.getVal().keySet()) {
                if (cList == null)
                    error(Holder.getM().getMess(IErrorCode.ERRORCODE35,
                            ILogMess.CANNOTFINDCHECKLIST,
                            ICommonConsts.JCHECKLISTMAP, s));
                PyList pList = createList(cList.constructValLine(), var
                        .getVal().get(s));
                m.put(toString(s), pList);
            }
            PyDictionary elemMap = new PyDictionary(m);
            pyMap.put(toString(checkS), elemMap);
        }
        pMap.put(JCHECKMAP, new PyDictionary(pyMap));
    }

    @SuppressWarnings("rawtypes")
    private static void extractListFromSeq(ListOfRows lRows, RowIndex rI,
            PyList pList, DialogFormat d) {
        ListIterator i = pList.listIterator();
        while (i.hasNext()) {
            Object e = i.next();
            PyDictionary vMap = (PyDictionary) e;
            DialogVariables v = new DialogVariables();
            toDialogVariables(rI.getColList(), v, vMap);
            RowContent row = rI.constructRow();
            for (String s : v.getFields()) {
                FieldValue valF = v.getValue(s);
                if (!rI.isField(s)) {
                    error(Holder.getM().getMess(IErrorCode.ERRORCODE28,
                            ILogMess.COLUMNNOTDEFINED, s, d.getId()));
                }
                rI.setRowField(row, s, valF);
            }
            lRows.addRow(row);
        }
    }

    private static abstract class IterateMap {
        private final PyDictionary pyMap;
        private final boolean intExpected;
        private final boolean mapExpected;
        protected int intFound;
        protected PyDictionary fMap;

        IterateMap(PyDictionary pyMap, boolean intExpected, boolean mapExpected) {
            this.pyMap = pyMap;
            this.intExpected = intExpected;
            this.mapExpected = mapExpected;
        }

        abstract void visit(String listId, PyList pList);

        void runMap() {
            PyObject item = pyMap.iteritems();
            PyIterator iter = (PyIterator) item;
            PyObject next;
            while ((next = iter.__iternext__()) != null) {
                PyTuple tu = (PyTuple) next;
                Object key = tu.get(0);
                Object val = tu.get(1);
                String listId = (String) key;
                PyList pList = null;
                if (mapExpected && val instanceof PyDictionary) {
                    fMap = (PyDictionary) val;
                } else if (intExpected && val instanceof Integer) {
                    intFound = ((Integer) val).intValue();
                } else {
                    pList = (PyList) val;
                }
                visit(listId, pList);
            }
        }
    }

    private static class ExtractList extends IterateMap {

        private final DialogFormat d;
        private final String actionId;
        private final DialogVariables vOut;

        ExtractList(PyDictionary pyMap, DialogFormat d, String actionId,
                DialogVariables vOut) {
            super(pyMap, true, false);
            this.d = d;
            this.actionId = actionId;
            this.vOut = vOut;
        }

        @Override
        void visit(String listId, PyList pList) {
            if (d.getListList().isEmpty())
                error(Holder.getM().getMess(IErrorCode.ERRORCODE38,
                        ILogMess.EMPTYLISTDEFINITION, listId, d.getId()));

            ListFormat lForm = d.findList(listId);
            if (lForm == null)
                error(Holder.getM().getMess(IErrorCode.ERRORCODE39, d.getId(),
                        listId));
            RowIndex rI = new RowIndex(lForm.getColumns());
            ListOfRows lRows = new ListOfRows();

            if (pList == null) {
                if (!lForm.isChunked()) {
                    String errmess = Holder.getM().getMess(
                            IErrorCode.ERRORCODE50, ILogMess.SEQUENCEEXPECTED,
                            d.getId(), lForm.getId());
                    error(errmess);
                } else {
                    lRows.setSize(intFound);
                }
            } else {
                if (lForm.isChunked() && actionId.equals(ICommonConsts.BEFORE)) {
                    error(d.getId() + " " + lForm.getId()
                            + " list is chunked. Sequence size is expected");
                }
                extractListFromSeq(lRows, rI, pList, d);
            }
            vOut.setRowList(listId, lRows);
        }

    }

    private static void extractList(PyDictionary pyMap, DialogVariables vOut,
            DialogFormat d, String actionId) {
        ExtractList eList = new ExtractList(pyMap, d, actionId, vOut);
        eList.runMap();
    }

    private static class ExtractDataLineList extends IterateMap {

        private final DialogFormat d;
        private final DialogVariables vOut;

        ExtractDataLineList(PyDictionary pyMap, DialogFormat d,
                DialogVariables vOut) {
            super(pyMap, false, false);
            this.d = d;
            this.vOut = vOut;
        }

        @Override
        void visit(String listId, PyList pList) {
            DateLine dLine = d.findDateLine(listId);
            if (dLine == null) {
                String mess = Holder.getM().getMess(IErrorCode.ERRORCODE49,
                        ILogMess.DATELINENOTDEFINED, listId,
                        ICommonConsts.DATELINE, d.getId());
                error(mess);
            }
            List<FieldItem> seqList = dLine.getColList();
            RowIndex rI = new RowIndex(seqList);
            DateLineVariables lineVariables = new DateLineVariables();
            extractListFromSeq(lineVariables.getLines(), rI, pList, d);
            vOut.getDatelineVariables().put(listId, lineVariables);
        }

    }

    private static class ExtractCheckList extends IterateMap {

        private final DialogFormat d;
        private final DialogVariables vOut;

        ExtractCheckList(PyDictionary pyMap, DialogFormat d,
                DialogVariables vOut) {
            super(pyMap, false, true);
            this.d = d;
            this.vOut = vOut;
        }

        private class ExtractListContent extends IterateMap {

            private final String checkId;

            ExtractListContent(PyDictionary pyMap, String checkId) {
                super(pyMap, false, false);
                this.checkId = checkId;
            }

            @Override
            void visit(String listId, PyList pList) {
                CheckList cList = DialogFormat.findE(d.getCheckList(), checkId);
                if (cList == null) {
                    error(Holder.getM().getMess(IErrorCode.ERRORCODE31,
                            ILogMess.CANNOTFINDCHECKLIST,
                            ICommonConsts.JCHECKLISTMAP, listId));
                }
                List<FieldItem> seqList = null;
                ListOfRows lRows = null;
                DialogCheckVariables checkVariables = vOut.getCheckVariables()
                        .get(checkId);
                if (checkVariables == null) {
                    checkVariables = new DialogCheckVariables();
                    vOut.getCheckVariables().put(checkId, checkVariables);
                }

                if (listId.equals(ICommonConsts.CHECKLISTLINES)) {
                    seqList = cList.getLines().constructCol();
                    lRows = checkVariables.getLines();
                }
                if (listId.equals(ICommonConsts.CHECKLISTCOLUMNS)) {
                    seqList = cList.getColumns().constructCol();
                    lRows = checkVariables.getColumns();
                }
                if (listId.equals(ICommonConsts.JERROR)) {
                    seqList = cList.constructErrLine();
                    lRows = checkVariables.getErrors();
                }
                if (seqList != null) {
                    RowIndex rI = new RowIndex(seqList);
                    extractListFromSeq(lRows, rI, pList, d);
                    return;
                }
                RowIndex rI = new RowIndex(cList.constructValLine());
                lRows = new ListOfRows();
                extractListFromSeq(lRows, rI, pList, d);

                checkVariables.getVal().put(listId, lRows);
            }

        }

        @Override
        void visit(String listId, PyList pList) {
            ExtractListContent extract = new ExtractListContent(fMap, listId);
            extract.runMap();
        }

    }

    private static void toDialogVariables(List<FieldItem> fList,
            DialogVariables v, PyDictionary pyMap) {
        PyObject item = pyMap.iteritems();
        PyIterator iter = (PyIterator) item;
        PyObject next;
        while ((next = iter.__iternext__()) != null) {
            PyTuple tu = (PyTuple) next;
            Object key = tu.get(0);
            Object val = tu.get(1);
            if (key.equals(ICommonConsts.JLISTMAP)) {
                continue;
            }
            if (key.equals(ICommonConsts.JCHECKLISTMAP)) {
                continue;
            }
            if (key.equals(ICommonConsts.JDATELINEMAP)) {
                continue;
            }
            String keyS = (String) key;
            TT fType = null;
            FieldValue valFF = v.getValue(keyS);
            FieldItem ffItem = null;
            if (fList != null)
                ffItem = DialogFormat.findE(fList, keyS);
            int afterdot = ICommonConsts.DEFAULTAFTERDOT;
            if (valFF != null) {
                fType = valFF.getType();
                afterdot = valFF.getAfterdot();
            } else {
                if (ffItem != null) {
                    fType = ffItem.getFieldType();
                    afterdot = ffItem.getAfterDot();
                }
            }
            FieldValue f = new FieldValue();
            if (fType != null) {
                if (val == null) {
                    f.setValue(fType, null, afterdot);
                } else
                    switch (fType) {
                    case STRING:
                        f.setValue((String) val);
                        break;
                    case BOOLEAN:
                        f.setValue((Boolean) val);
                        break;
                    case INT:
                        Integer inte;
                        if (val instanceof Integer) {
                            inte = (Integer) val;
                        } else {
                            BigInteger b = (BigInteger) val;
                            inte = b.intValue();
                        }
                        f.setValue(inte);
                        break;
                    case LONG:
                        Long lV = null;
                        if (val instanceof Integer) {
                            lV = new Long((Integer) val);
                        } else if (val instanceof BigInteger) {
                            BigInteger bi = (BigInteger) val;
                            lV = bi.longValue();
                        } else
                            error(Holder.getM().getMess(IErrorCode.ERRORCODE40,
                                    ILogMess.INTEGERORBITINTEGEREXPECTED, keyS));

                        f.setValue(lV);
                        break;
                    case BIGDECIMAL:
                        BigDecimal bV = null;
                        if (val instanceof Integer) {
                            bV = new BigDecimal((Integer) val);
                        } else if (val instanceof BigInteger) {
                            bV = new BigDecimal((BigInteger) val);
                        } else if (val instanceof Double) {
                            bV = new BigDecimal((Double) val);
                        } else if (val instanceof BigDecimal) {
                            bV = (BigDecimal) val;
                        } else
                            error(Holder.getM().getMess(
                                    IErrorCode.ERRORCODE41,
                                    ILogMess.INTEGERORBITINTEGEREXPECTED,
                                    keyS,
                                    val == null ? null : val.getClass()
                                            .getName()));

                        BigDecimal bx = bV.setScale(afterdot,
                                BigDecimal.ROUND_HALF_UP);
                        f.setValue(bx, afterdot);
                        break;
                    case DATE:
                        java.sql.Date dt = (java.sql.Date) val;
                        Date dat = new Date(dt.getTime());
                        f.setValue(dat);
                        break;
                    case DATETIME:
                        Timestamp ti;
                        if (val instanceof java.sql.Date) {
                            java.sql.Date dti = (java.sql.Date) val;
                            ti = new Timestamp(dti.getTime());
                        } else {
                            ti = (Timestamp) val;
                        }
                        f.setValue(ti);
                        break;
                    default:
                        error(Holder.getM().getMess(IErrorCode.ERRORCODE32,
                                ILogMess.TYPEMAPNOTIMPLEMENTS, keyS,
                                fType.toString()));
                    }
            } else {
                if (val == null) {
                    error(Holder.getM().getMess(IErrorCode.ERRORCODE30,
                            ILogMess.VALUECANNOTBENULL, keyS));
                }
                if (val instanceof String) {
                    String valS = (String) val;
                    f.setValue(valS);
                } else if (val instanceof Boolean) {
                    Boolean valB = (Boolean) val;
                    f.setValue(valB);
                } else if (val instanceof Integer) {
                    Integer valI = (Integer) val;
                    f.setValue(valI);
                } else if (val instanceof Long) {
                    Long valL = (Long) val;
                    f.setValue(valL);
                } else {
                    error(Holder.getM().getMess(IErrorCode.ERRORCODE29,
                            ILogMess.TYPENOTIMPLEMENTED, keyS));
                }
            }
            v.setValue(keyS, f);
        }
    }

    /**
     * Add new path to Jython sys.path if not added already
     * 
     * @param interp
     *            PythonIntepreter
     * @param addPath
     *            Path to add if not added
     */

    static private void addIfNotExisttoPath(PythonInterpreter interp,
            String addPath) {
        interp.exec("import sys; " + GGTempVariable + "= sys.path");
        PyObject po = interp.get(GGTempVariable);
        PyList pyList = (PyList) po;
        ListIterator iL = pyList.listIterator();
        boolean exist = false;
        while (iL.hasNext()) {
            String path = (String) iL.next();
            if (path.equals(addPath)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            interp.exec("import sys");
            // interp.exec("print sys.path");
            putDebug("append sys.path = " + addPath);
            interp.exec("sys.path.append('" + addPath + "')");
        }

    }

    private static void executeJythonforDialog(IJythonUIServerProperties p,
            MCached mCached, DialogVariables v, DialogFormat d, String actionId) {
        // checked by experience that if file.encoding is not null then
        // Jython cannot be started in Development Mode
        // but it works as expected after deploying to Google App Engine
        System.getProperties().remove("file.encoding");
        String importJ = d.getJythonImport();
        putDebug("import jython = " + importJ);
        String methodJ = d.getJythonMethod();
        putDebug("method jython = " + methodJ);
        if (methodJ == null) {
            error(Holder.getM().getMess(IErrorCode.ERRORCODE42,
                    ILogMess.METHODNOTDEFINED, d.getId(), actionId));
        }

        /*
         * According to Jython documentation PythonInterpreter is thread
         * confined. So this code is thread safe.
         */
        PythonInterpreter interp;
        if (mCached.isCached()) {
            // Checked by experience that default PythonIntepreter constructor
            // keeps compiled packages, so we cannot modify jython source code
            // dynamically, server restart is necessary
            // TODO: check in docs if it is expected behavior or side effect
            interp = new PythonInterpreter();
        } else {
            // Constructor with parameters forces package recompiling
            // but there is a performance penalty
            interp = new PythonInterpreter(null, new PySystemState());
            interp.cleanup();
        }

        // check sys.path
        addIfNotExisttoPath(interp, p.getPackageDirectory());

        Map<PyObject, PyObject> pMap = toPythonMap(v);
        addListToMap(pMap, d, v);
        addCheckListToMap(pMap, d, v);
        PyDictionary pyMap = new PyDictionary(pMap);

        interp.set(GGTempVariable, pyMap);
        interp.set(AATempVariable, actionId);

        String s = MessageFormat
                .format(methodJ, AATempVariable, GGTempVariable);
        if (importJ != null) {
            s = importJ + "; " + s;
        }
        putDebug(s);
        interp.exec(s);
        toDialogVariables(d.getFieldList(), v, pyMap);
        if (pyMap.has_key(JLISTMAP)) {
            PyObject o = pyMap.__getitem__(JLISTMAP);
            extractList((PyDictionary) o, v, d, actionId);
        }
        if (pyMap.has_key(JCHECKMAP)) {
            PyObject o = pyMap.__getitem__(JCHECKMAP);
            ExtractCheckList e = new ExtractCheckList((PyDictionary) o, d, v);
            e.runMap();
        }
        if (pyMap.has_key(JDATELINEMAP)) {
            PyObject o = pyMap.__getitem__(JDATELINEMAP);
            ExtractDataLineList e = new ExtractDataLineList((PyDictionary) o,
                    d, v);
            e.runMap();
        }
    }

    private static void copyAttr(ElemDescription dest, ElemDescription sou,
            String attrName) {
        String val = sou.getAttr(attrName);
        if (!CUtil.EmptyS(val))
            dest.setAttr(attrName, val);
    }

    private static void executeForType(IJythonUIServerProperties p,
            MCached mCached, DialogVariables v, DialogFormat d, String idType,
            boolean comboNow) {
        if (d.getTypeList() == null) {
            return;
        }
        for (TypesDescr t : d.getTypeList()) {
            if (v.getEnumList().containsKey(idType)) {
                return;
            }
            TypedefDescr ty = DialogFormat.findE(t.getTypeList(), idType);
            if (ty.isHelperType() && comboNow) {
                return;
            }
            if (ty.isComboType() && !comboNow) {
                return;
            }
            DialogVariables va = new DialogVariables();
            DialogFormat dd = new DialogFormat();
            copyAttr(dd, t, ICommonConsts.IMPORT);
            copyAttr(dd, t, ICommonConsts.METHOD);
            copyAttr(dd, ty, ICommonConsts.IMPORT);
            copyAttr(dd, ty, ICommonConsts.METHOD);
            ListFormat li = new ListFormat();
            li.setId(ty.getId());
            li.getColumns().addAll(ty.getListOfColumns());
            dd.getListList().add(li);
            // copy values (variables only)
            for (String f : v.getFields()) {
                FieldValue val = v.getValue(f);
                va.setValue(f, val);
            }
            executeJythonforDialog(p, mCached, va, dd, idType);
            v.getEnumList().putAll(va.getRowList());
            return;
        }
        error(idType + " unrecognized custom type");
    }

    static private void executeForField(IJythonUIServerProperties p,
            MCached mCached, DialogVariables v, DialogFormat d,
            List<FieldItem> fList) {
        for (FieldItem i : fList) {
            String t = i.getCustom();
            if (CUtil.EmptyS(t)) {
                continue;
            }
            executeForType(p, mCached, v, d, t, true);
        }
    }

    static private void executeForEnum(IJythonUIServerProperties p,
            MCached mCached, DialogVariables v, DialogFormat d) {
        executeForField(p, mCached, v, d, d.getFieldList());
        for (ListFormat li : d.getListList()) {
            executeForField(p, mCached, v, d, li.getColumns());
        }
    }

    // entry point

    public static void executeJython(IJythonUIServerProperties p,
            MCached mCached, DialogVariables v, DialogFormat d, String actionId) {
        String cu = FieldItem.getCustomT(actionId);
        if (!CUtil.EmptyS(cu)) {
            executeForType(p, mCached, v, d, cu, false);
            return;
        }
        executeJythonforDialog(p, mCached, v, d, actionId);
        if (actionId.equals(ICommonConsts.BEFORE)) {
            executeForEnum(p, mCached, v, d);
        }
    }

}
