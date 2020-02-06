// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.appslandia.common.base.DateFormatException;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class DateUtils {

	// ISO8601
	public static final String PATTERN_DATE = "yyyy-MM-dd";

	public static final String PATTERN_TIME = "HH:mm:ss.SSS";
	public static final String PATTERN_TIME_Z = "HH:mm:ss.SSSXXX";

	public static final String PATTERN_TIME_MI = "HH:mm";
	public static final String PATTERN_TIME_MIZ = "HH:mmXXX";

	public static final String PATTERN_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final String PATTERN_DATETIME_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static final String PATTERN_DATETIME_MI = "yyyy-MM-dd'T'HH:mm";
	public static final String PATTERN_DATETIME_MIZ = "yyyy-MM-dd'T'HH:mmXXX";

	private static final Pattern TEMPORAL_AMT_PATTERN = Pattern.compile("((\\d+.\\d+|\\d+)(wk|d|hr|min|sec|ms)\\s*)+", Pattern.CASE_INSENSITIVE);

	public static long translateToMs(String temporalAmt) throws IllegalArgumentException {
		temporalAmt = StringUtils.trimToNull(temporalAmt);
		AssertUtils.assertNotNull(temporalAmt, "temporalAmt is required.");
		AssertUtils.assertTrue(TEMPORAL_AMT_PATTERN.matcher(temporalAmt).matches(), "temporalAmt is invalid format.");

		double result = 0l;
		int i = 0;

		while (i < temporalAmt.length()) {
			int j = i;
			while (Character.isDigit(temporalAmt.charAt(j)))
				j++;
			int k = j;
			while ((k <= temporalAmt.length() - 1) && (Character.isLetter(temporalAmt.charAt(k)) || (temporalAmt.charAt(k) == (' '))))
				k++;

			double amt = Double.parseDouble(temporalAmt.substring(i, j));
			String unit = temporalAmt.substring(j, k).trim();

			if ("wk".equalsIgnoreCase(unit)) {
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) * 7 * amt;
			} else if ("d".equalsIgnoreCase(unit)) {
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) * amt;
			} else if ("hr".equalsIgnoreCase(unit)) {
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS) * amt;
			} else if ("min".equalsIgnoreCase(unit)) {
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES) * amt;
			} else if ("sec".equalsIgnoreCase(unit)) {
				result += TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS) * amt;
			} else {
				result += amt;
			}
			i = k;
		}
		return (long) Math.ceil(result);
	}

	public static Map<TimeUnit, Long> parseUnits(long durationMs, TimeUnit start, TimeUnit end) {
		return parseUnits(durationMs, TimeUnit.MILLISECONDS, start, end);
	}

	public static Map<TimeUnit, Long> parseUnits(long duration, TimeUnit unit, TimeUnit start, TimeUnit end) {
		AssertUtils.assertTrue(start.compareTo(end) >= 0);
		Map<TimeUnit, Long> map = new EnumMap<>(TimeUnit.class);

		long ms = TimeUnit.MILLISECONDS.convert(duration, unit);
		TimeUnit u = start;
		while (true) {
			long v = u.convert(ms, TimeUnit.MILLISECONDS);
			map.put(u, v);
			if (u == end) {
				break;
			}
			ms -= TimeUnit.MILLISECONDS.convert(v, u);
			u = nextUnit(u);
		}
		return map;
	}

	private static TimeUnit nextUnit(TimeUnit unit) {
		switch (unit) {
		case DAYS:
			return TimeUnit.HOURS;
		case HOURS:
			return TimeUnit.MINUTES;
		case MINUTES:
			return TimeUnit.SECONDS;
		case SECONDS:
			return TimeUnit.MILLISECONDS;
		default:
			throw new IllegalArgumentException();
		}
	}

	public static boolean isFutureTime(long timeMillis, int leewayMs) {
		AssertUtils.assertTrue(leewayMs >= 0);
		return System.currentTimeMillis() - leewayMs < timeMillis;
	}

	public static boolean isPastTime(long timeMillis, int leewayMs) {
		AssertUtils.assertTrue(leewayMs >= 0);
		return System.currentTimeMillis() + leewayMs >= timeMillis;
	}

	public static java.sql.Date today() {
		return new java.sql.Date(todayCalendar().getTimeInMillis());
	}

	public static long todayAsLong() {
		return todayCalendar().getTimeInMillis();
	}

	public static Calendar todayCalendar() {
		Calendar cal = new GregorianCalendar();
		clearTime(cal);
		return cal;
	}

	public static long utcNow() {
		return System.currentTimeMillis();
	}

	public static long utcNowNoMs() {
		return clearMs(System.currentTimeMillis());
	}

	public static java.sql.Timestamp now() {
		return new java.sql.Timestamp(utcNow());
	}

	public static java.sql.Timestamp nowNoMs() {
		return new java.sql.Timestamp(utcNowNoMs());
	}

	public static long clearMs(long timeInMs) {
		return (timeInMs / 1000) * 1000;
	}

	public static Date clearMs(Date dt) {
		return new Date(clearMs(dt.getTime()));
	}

	public static long clearTime(long timeInMs) {
		return clearTime(new Date(timeInMs)).getTime();
	}

	public static Date clearTime(Date dt) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		clearTime(cal);
		return cal.getTime();
	}

	public static void clearTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	public static Calendar getCalendar(int dayOfWeek, int atHour, int atMinute) {
		Calendar cal = new GregorianCalendar();

		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		cal.set(Calendar.HOUR_OF_DAY, atHour);
		cal.set(Calendar.MINUTE, atMinute);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Date copyTime(Date dest, Date src) {
		Calendar destCal = new GregorianCalendar();
		destCal.setTime(dest);

		Calendar srcCal = new GregorianCalendar();
		srcCal.setTime(src);

		destCal.set(Calendar.HOUR_OF_DAY, srcCal.get(Calendar.HOUR_OF_DAY));
		destCal.set(Calendar.MINUTE, srcCal.get(Calendar.MINUTE));
		destCal.set(Calendar.SECOND, srcCal.get(Calendar.SECOND));
		destCal.set(Calendar.MILLISECOND, srcCal.get(Calendar.MILLISECOND));
		return destCal.getTime();
	}

	public static String format(Date dt, String pattern) {
		return newDateFormat(pattern).format(dt);
	}

	public static SimpleDateFormat newDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf;
	}

	public static java.sql.Date iso8601Date(String date) throws DateFormatException {
		return (date != null) ? new java.sql.Date(parse(date, PATTERN_DATE).getTime()) : null;
	}

	public static String iso8601Date(Date date) {
		return (date != null) ? newDateFormat(PATTERN_DATE).format(date) : null;
	}

	public static java.sql.Time iso8601Time(String time) throws DateFormatException {
		return (time != null) ? new java.sql.Time(parse(time, PATTERN_TIME).getTime()) : null;
	}

	public static String iso8601Time(Date time) {
		return (time != null) ? newDateFormat(PATTERN_TIME).format(time) : null;
	}

	public static java.sql.Timestamp iso8601DateTime(String dateTime) throws DateFormatException {
		return (dateTime != null) ? new java.sql.Timestamp(parse(dateTime, PATTERN_DATETIME).getTime()) : null;
	}

	public static String iso8601DateTime(Date dateTime) {
		return (dateTime != null) ? newDateFormat(PATTERN_DATETIME).format(dateTime) : null;
	}

	public static Date parse(String dt, String pattern) throws DateFormatException {
		try {
			return newDateFormat(pattern).parse(dt);
		} catch (ParseException ex) {
			throw new DateFormatException(ex);
		}
	}
}
