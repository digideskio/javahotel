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
package com.javahotel.common.dateutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CalendarTable {

    private CalendarTable() {
    }

    private static boolean endOfWeek(final Date cal) {
        int dOfWeek = cal.getDay();
        return (dOfWeek == 0);
    }

    private static boolean endOfMonth(final Date cal) {
        Date d = DateUtil.copyDate(cal);
        DateUtil.NextDay(d);
        int da = d.getDate();
        return da == 1;
    }

    public enum PeriodType {

        byMonth, byWeek, byDay;
    }

    public static List<Date> listOfDates(final Date first,
            final Date last, final PeriodType pType) {
        // get beginning of first week
        Date actC = DateUtil.copyDate(first);
        List<Date> cDays = new ArrayList<Date>();
        boolean ladded = false;

        while (DateUtil.compareDate(actC, last) != 1) {
            boolean addd = false;
            switch (pType) {
                case byMonth:
                    addd = endOfMonth(actC);
                    break;
                case byWeek:
                    addd = endOfWeek(actC);
                    break;
                case byDay:
                    addd = true;
                    break;
            }
            if (addd) {
                cDays.add(DateUtil.copyDate(actC));
                if (DateUtil.eqDate(actC, last)) {
                    ladded = true;
                }
            }
            DateUtil.NextDay(actC);
        }
        if (!ladded) {
            cDays.add(last);
        }
        return cDays;
    }
}
