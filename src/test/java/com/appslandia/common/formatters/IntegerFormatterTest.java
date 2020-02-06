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
public class IntegerFormatterTest {

	IntegerFormatter integerFormatter;

	@Before
	public void initialize() {
		integerFormatter = new IntegerFormatter();
		Assert.assertEquals(integerFormatter.getArgType(), Integer.class);
	}

	@Test
	public void test() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.VI);
		try {
			Integer v = integerFormatter.parse("12345", formatProvider);
			Assert.assertEquals(v.intValue(), 12345);

			v = integerFormatter.parse("12.345", formatProvider);
			Assert.assertEquals(v.intValue(), 12345);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Integer val = integerFormatter.parse(null, formatProvider);
			Assert.assertNull(val);

			val = integerFormatter.parse("", formatProvider);
			Assert.assertNull(val);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			integerFormatter.parse("12-345", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_invalidDecimals() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			integerFormatter.parse("12,345.55", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_maxMin() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Integer v = integerFormatter.parse(Integer.toString(Integer.MAX_VALUE), formatProvider);
			Assert.assertEquals(v.intValue(), Integer.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Integer v = integerFormatter.parse(Integer.toString(Integer.MIN_VALUE), formatProvider);
			Assert.assertEquals(v.intValue(), Integer.MIN_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_overflow() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			// Max: 2147483647
			integerFormatter.parse("2147483648", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
			Assert.assertTrue(ex.getMessage().contains("overflow"));
		}

		try {
			// MIN: -2147483648
			integerFormatter.parse("-2147483649", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
			Assert.assertTrue(ex.getMessage().contains("overflow"));
		}
	}
}
