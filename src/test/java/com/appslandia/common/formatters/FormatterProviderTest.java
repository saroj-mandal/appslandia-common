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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class FormatterProviderTest {

	@Test
	public void test() {
		FormatterProvider provider = new FormatterProvider();
		try {
			Formatter formatter = provider.getFormatter(String.class);
			Assert.assertTrue(formatter.getClass() == StringFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Byte.class);
			Assert.assertTrue(formatter.getClass() == ByteFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Short.class);
			Assert.assertTrue(formatter.getClass() == ShortFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Integer.class);
			Assert.assertTrue(formatter.getClass() == IntegerFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Long.class);
			Assert.assertTrue(formatter.getClass() == LongFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Float.class);
			Assert.assertTrue(formatter.getClass() == FloatFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Double.class);
			Assert.assertTrue(formatter.getClass() == DoubleFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(BigDecimal.class);
			Assert.assertTrue(formatter.getClass() == BigDecimalFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Boolean.class);
			Assert.assertTrue(formatter.getClass() == BooleanFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(java.sql.Date.class);
			Assert.assertTrue(formatter.getClass() == SqlDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(java.sql.Time.class);
			Assert.assertTrue(formatter.getClass() == SqlTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(java.sql.Timestamp.class);
			Assert.assertTrue(formatter.getClass() == SqlTimestampFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_formatterIds() {
		FormatterProvider provider = new FormatterProvider();
		try {
			Formatter formatter = provider.getFormatter(Formatter.STRING);
			Assert.assertTrue(formatter.getClass() == StringFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.UPPER);
			Assert.assertTrue(formatter.getClass() == LowerUpperFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOWER);
			Assert.assertTrue(formatter.getClass() == LowerUpperFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.EN_UPPER);
			Assert.assertTrue(formatter.getClass() == LowerUpperFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.EN_LOWER);
			Assert.assertTrue(formatter.getClass() == LowerUpperFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.INVARIANT_UPPER);
			Assert.assertTrue(formatter.getClass() == LowerUpperFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.INVARIANT_LOWER);
			Assert.assertTrue(formatter.getClass() == LowerUpperFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BYTE);
			Assert.assertTrue(formatter.getClass() == ByteFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.SHORT);
			Assert.assertTrue(formatter.getClass() == ShortFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.INTEGER);
			Assert.assertTrue(formatter.getClass() == IntegerFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LONG);
			Assert.assertTrue(formatter.getClass() == LongFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.FLOAT);
			Assert.assertTrue(formatter.getClass() == FloatFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DOUBLE);
			Assert.assertTrue(formatter.getClass() == DoubleFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BIGDECIMAL);
			Assert.assertTrue(formatter.getClass() == BigDecimalFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.SHORT_LZ);
			Assert.assertTrue(formatter.getClass() == ShortFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.INTEGER_LZ);
			Assert.assertTrue(formatter.getClass() == IntegerFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LONG_LZ);
			Assert.assertTrue(formatter.getClass() == LongFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.FLOAT_LZ);
			Assert.assertTrue(formatter.getClass() == FloatFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DOUBLE_LZ);
			Assert.assertTrue(formatter.getClass() == DoubleFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BIGDECIMAL_LZ);
			Assert.assertTrue(formatter.getClass() == BigDecimalFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.BOOLEAN);
			Assert.assertTrue(formatter.getClass() == BooleanFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DATE);
			Assert.assertTrue(formatter.getClass() == SqlDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIME);
			Assert.assertTrue(formatter.getClass() == SqlTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP);
			Assert.assertTrue(formatter.getClass() == SqlTimestampFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIME_MI);
			Assert.assertTrue(formatter.getClass() == SqlTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP_MI);
			Assert.assertTrue(formatter.getClass() == SqlTimestampMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.DATE_LZ);
			Assert.assertTrue(formatter.getClass() == SqlDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP_LZ);
			Assert.assertTrue(formatter.getClass() == SqlTimestampFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.TIMESTAMP_MILZ);
			Assert.assertTrue(formatter.getClass() == SqlTimestampMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_jdk8Dates() {
		FormatterProvider provider = new FormatterProvider();
		Jdk8FormatterUtils.register(provider);

		try {
			Formatter formatter = provider.getFormatter(LocalDate.class);
			Assert.assertTrue(formatter.getClass() == LocalDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(LocalTime.class);
			Assert.assertTrue(formatter.getClass() == LocalTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(LocalDateTime.class);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(OffsetTime.class);
			Assert.assertTrue(formatter.getClass() == OffsetTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(OffsetDateTime.class);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_jdk8Dates_formatterIds() {
		FormatterProvider provider = new FormatterProvider();
		Jdk8FormatterUtils.register(provider);

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATE);
			Assert.assertTrue(formatter.getClass() == LocalDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_TIME);
			Assert.assertTrue(formatter.getClass() == LocalTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_TIME_MI);
			Assert.assertTrue(formatter.getClass() == LocalTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME_MI);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_TIME);
			Assert.assertTrue(formatter.getClass() == OffsetTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_TIME_MI);
			Assert.assertTrue(formatter.getClass() == OffsetTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME_MI);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATE_LZ);
			Assert.assertTrue(formatter.getClass() == LocalDateFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME_LZ);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.LOCAL_DATETIME_MILZ);
			Assert.assertTrue(formatter.getClass() == LocalDateTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME_LZ);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Formatter formatter = provider.getFormatter(Formatter.OFFSET_DATETIME_MILZ);
			Assert.assertTrue(formatter.getClass() == OffsetDateTimeMIFormatter.class);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}
}
