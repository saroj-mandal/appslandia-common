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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Jdk8DateUtils {

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATE);

	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME);
	public static final DateTimeFormatter TIME_FORMATTER_Z = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME_Z);

	public static final DateTimeFormatter TIME_FORMATTER_MI = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME_MI);
	public static final DateTimeFormatter TIME_FORMATTER_MIZ = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME_MIZ);

	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME);
	public static final DateTimeFormatter DATETIME_FORMATTER_Z = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME_Z);

	public static final DateTimeFormatter DATETIME_FORMATTER_MI = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME_MI);
	public static final DateTimeFormatter DATETIME_FORMATTER_MIZ = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME_MIZ);

	public static OffsetDateTime utcNow() {
		return offsetNow(ZoneOffset.UTC);
	}

	public static OffsetDateTime utcNowNoMs() {
		return offsetNow(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
	}

	public static OffsetDateTime offsetNow(String offsetId) {
		return offsetNow(ZoneOffset.of(offsetId));
	}

	public static OffsetDateTime offsetNow(ZoneOffset offset) {
		return OffsetDateTime.now().withOffsetSameInstant(offset);
	}

	public static LocalDateTime toLocalDateTime(Date dateTime, String atZoneId) {
		return LocalDateTime.ofInstant(dateTime.toInstant(), (atZoneId == null) ? ZoneId.systemDefault() : ZoneId.of(atZoneId));
	}

	public static Date toUtilDate(LocalDateTime localDateTime, String atZoneId) {
		return new Date(localDateTime.atZone((atZoneId == null) ? ZoneId.systemDefault() : ZoneId.of(atZoneId)).toInstant().toEpochMilli());
	}

	public static LocalDate iso8601Date(String date) throws DateTimeParseException {
		return (date != null) ? LocalDate.parse(date, DATE_FORMATTER) : null;
	}

	public static String iso8601Date(LocalDate date) {
		return (date != null) ? DATE_FORMATTER.format(date) : null;
	}

	public static LocalTime iso8601Time(String time) throws DateTimeParseException {
		return (time != null) ? LocalTime.parse(time, TIME_FORMATTER) : null;
	}

	public static String iso8601Time(LocalTime time) {
		return (time != null) ? TIME_FORMATTER.format(time) : null;
	}

	public static LocalDateTime iso8601DateTime(String dateTime) throws DateTimeParseException {
		return (dateTime != null) ? LocalDateTime.parse(dateTime, DATETIME_FORMATTER) : null;
	}

	public static String iso8601DateTime(LocalDateTime dateTime) {
		return (dateTime != null) ? DATETIME_FORMATTER.format(dateTime) : null;
	}

	public static OffsetTime iso8601TimeZ(String timeZ) throws DateTimeParseException {
		return (timeZ != null) ? OffsetTime.parse(timeZ, TIME_FORMATTER_Z) : null;
	}

	public static String iso8601TimeZ(OffsetTime timeZ) {
		return (timeZ != null) ? TIME_FORMATTER_Z.format(timeZ) : null;
	}

	public static OffsetDateTime iso8601DateTimeZ(String dateTimeZ) throws DateTimeParseException {
		return (dateTimeZ != null) ? OffsetDateTime.parse(dateTimeZ, DATETIME_FORMATTER_Z) : null;
	}

	public static String iso8601DateTimeZ(OffsetDateTime dateTimeZ) {
		return (dateTimeZ != null) ? DATETIME_FORMATTER_Z.format(dateTimeZ) : null;
	}
}
