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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.appslandia.common.base.FormatProvider;
import com.appslandia.common.base.FormatProviderImpl;
import com.appslandia.common.base.Language;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class ShortFormatterTest {

	ShortFormatter shortFormatter;

	@Before
	public void initialize() {
		shortFormatter = new ShortFormatter();
		Assert.assertEquals(shortFormatter.getArgType(), Short.class);
	}

	@Test
	public void test() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.VI);
		try {
			Short v = shortFormatter.parse("12345", formatProvider);
			Assert.assertEquals(v.shortValue(), 12345);

			v = shortFormatter.parse("12.345", formatProvider);
			Assert.assertEquals(v.shortValue(), 12345);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Short val = shortFormatter.parse(null, formatProvider);
			Assert.assertNull(val);

			shortFormatter.parse("", formatProvider);
			Assert.assertNull(val);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			shortFormatter.parse("12-345", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_invalidDecimals() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			shortFormatter.parse("12,345.55", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_maxMin() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Short v = shortFormatter.parse(Short.toString(Short.MAX_VALUE), formatProvider);
			Assert.assertEquals(v.shortValue(), Short.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Short v = shortFormatter.parse(Short.toString(Short.MIN_VALUE), formatProvider);
			Assert.assertEquals(v.shortValue(), Short.MIN_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_overflow() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			// Max: 32767
			shortFormatter.parse("32768", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
			Assert.assertTrue(ex.getMessage().contains("overflow"));
		}

		try {
			// MIN: -32768
			shortFormatter.parse("-32769", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
			Assert.assertTrue(ex.getMessage().contains("overflow"));
		}
	}
}
