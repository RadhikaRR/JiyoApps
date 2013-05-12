package com.mw.customCalender;

import java.util.Calendar;

public class DateUtils {
	private static String MONTH_ARRAY[] = new String[] { "JAN", "FEB", "MAR",
			"APR", "MAY", "JUN", "JUL", "AUG", "SEP", "ACT", "NOV", "DEC" };

	public static int getFirstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getFirstDayOfMonth(Calendar cal) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static String getNameOfMonth(int month) {
		return MONTH_ARRAY[month];
	}

	public static String getFormatedDate() {
		Calendar calendar = Calendar.getInstance();
		return getFormatedDate(calendar);
	}

	public static String getFormatedDate(Calendar calendar) {
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		int day = calendar.get(Calendar.DATE);
		return day + "-" + MONTH_ARRAY[month] + "-" + year;
	}

	public static int getNumberOfDaysInMonth(int month, int year) {
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			return 31;
		case 3:
		case 5:
		case 8:
		case 10:
			return 30;
		case 1:
			if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
				return 29;
			} else {
				return 28;
			}
		}

		throw new IllegalArgumentException("Invalid Month");
	}
}
