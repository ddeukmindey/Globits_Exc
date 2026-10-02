package com.globits.hr.utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {
	public static long getDateDiff(Date date1, Date date2,TimeUnit timeUnit) {
	    long diffInMillie = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillie,TimeUnit.MILLISECONDS);
	}

	public static int lastDateOfMonth(int month, int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		return cal.getActualMaximum(Calendar.DATE);
	}

	public static int numberWeekOfMonth(int month, int year){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		if (cal.getActualMaximum(Calendar.DATE) <= 28){
			return 4;
		}
		return 5;
	}

	public static List<Date> getDateToDateOfWeek(int week, int month, int year) {
		List<Date> dateList = new ArrayList<>();
		int start = 0;
		int end = 0;
		if (week == 1){
			start = 1;
			end = 7;
		}
		if (week == 2){
			start = 8;
			end = 14;
		}
		if (week == 3){
			start = 15;
			end = 21;
		}
		if (week == 4){
			start = 22;
			end = 28;
		}
		if (week == 5){
			start = 29;
			end = lastDateOfMonth(month, year);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, start, 0, 0, 0);
		Date startDate = cal.getTime();
		dateList.add(startDate);
		cal.set(year, month - 1, end, 23, 59, 59);
		Date endDate = cal.getTime();
		dateList.add(endDate);
		return dateList;
	}
}
