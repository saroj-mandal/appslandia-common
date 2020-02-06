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

package com.appslandia.common.formatters;

import com.appslandia.common.base.FormatProvider;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public interface Formatter {
	public static final String BYTE = "Byte";

	public static final String SHORT = "Short";
	public static final String SHORT_LZ = "ShortLZ";

	public static final String INTEGER = "Integer";
	public static final String INTEGER_LZ = "IntegerLZ";

	public static final String LONG = "Long";
	public static final String LONG_LZ = "LongLZ";

	public static final String FLOAT = "Float";
	public static final String FLOAT_LZ = "FloatLZ";

	public static final String DOUBLE = "Double";
	public static final String DOUBLE_LZ = "DoubleLZ";

	public static final String BIGDECIMAL = "BigDecimal";
	public static final String BIGDECIMAL_LZ = "BigDecimalLZ";

	public static final String TIME = "Time";
	public static final String TIME_MI = "TimeMI";

	public static final String DATE = "Date";
	public static final String DATE_LZ = "DateLZ";

	public static final String TIMESTAMP = "Timestamp";
	public static final String TIMESTAMP_LZ = "TimestampLZ";

	public static final String TIMESTAMP_MI = "TimestampMI";
	public static final String TIMESTAMP_MILZ = "TimestampMILZ";

	public static final String LOCAL_TIME = "LocalTime";
	public static final String LOCAL_TIME_MI = "LocalTimeMI";

	public static final String LOCAL_DATE = "LocalDate";
	public static final String LOCAL_DATE_LZ = "LocalDateLZ";

	public static final String LOCAL_DATETIME = "LocalDateTime";
	public static final String LOCAL_DATETIME_LZ = "LocalDateTimeLZ";

	public static final String LOCAL_DATETIME_MI = "LocalDateTimeMI";
	public static final String LOCAL_DATETIME_MILZ = "LocalDateTimeMILZ";

	public static final String OFFSET_TIME = "OffsetTime";
	public static final String OFFSET_TIME_MI = "OffsetTimeMI";

	public static final String OFFSET_DATETIME = "OffsetDateTime";
	public static final String OFFSET_DATETIME_LZ = "OffsetDateTimeLZ";

	public static final String OFFSET_DATETIME_MI = "OffsetDateTimeMI";
	public static final String OFFSET_DATETIME_MILZ = "OffsetDateTimeMILZ";

	public static final String BOOLEAN = "Boolean";
	public static final String STRING = "String";
	public static final String SINGLE_LINE = "SingleLine";
	public static final String TRIM_TO_EMPTY = "TrimToEmpty";

	public static final String UPPER = "Upper";
	public static final String LOWER = "Lower";

	public static final String EN_UPPER = "EnUpper";
	public static final String EN_LOWER = "EnLower";

	public static final String INVARIANT_UPPER = "InvariantUpper";
	public static final String INVARIANT_LOWER = "InvariantLower";

	default String getErrorMsgKey() {
		return getClass().getName() + ".message";
	}

	Class<?> getArgType();

	String format(Object obj, FormatProvider formatProvider);

	Object parse(String str, FormatProvider formatProvider) throws FormatterException;
}
