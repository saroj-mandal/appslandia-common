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
public class ByteFormatterTest {

	ByteFormatter byteFormatter;

	@Before
	public void initialize() {
		byteFormatter = new ByteFormatter();
		Assert.assertEquals(byteFormatter.getArgType(), Byte.class);
	}

	@Test
	public void test() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.VI);
		try {
			Byte v = byteFormatter.parse("123", formatProvider);
			Assert.assertEquals(v.byteValue(), 123);

			v = byteFormatter.parse("1.23", formatProvider);
			Assert.assertEquals(v.byteValue(), 123);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Byte val = byteFormatter.parse(null, formatProvider);
			Assert.assertNull(val);

			val = byteFormatter.parse("", formatProvider);
			Assert.assertNull(val);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			byteFormatter.parse("12-3", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_invalidDecimals() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			byteFormatter.parse("12.3", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}

	@Test
	public void test_maxMin() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Byte v = byteFormatter.parse(Byte.toString(Byte.MAX_VALUE), formatProvider);
			Assert.assertEquals(v.byteValue(), Byte.MAX_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}

		try {
			Byte v = byteFormatter.parse(Byte.toString(Byte.MIN_VALUE), formatProvider);
			Assert.assertEquals(v.byteValue(), Byte.MIN_VALUE);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_overflow() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			// Max: 127
			byteFormatter.parse("128", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
			Assert.assertTrue(ex.getMessage().contains("overflow"));
		}

		try {
			// MIN: -128
			byteFormatter.parse("-129", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
			Assert.assertTrue(ex.getMessage().contains("overflow"));
		}
	}
}
