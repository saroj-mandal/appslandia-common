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
public class FloatFormatterTest {

	FloatFormatter floatFormatter;

	@Before
	public void initialize() {
		floatFormatter = new FloatFormatter();
		Assert.assertEquals(floatFormatter.getArgType(), Float.class);
	}

	@Test
	public void test() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Float v = floatFormatter.parse("12,345", formatProvider);
			Assert.assertEquals(v.floatValue(), 12345.000, 0.001);

			v = floatFormatter.parse("12.345", formatProvider);
			Assert.assertEquals(v.floatValue(), 12.345, 0.001);

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_decimals() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Float v = floatFormatter.parse("12,345.67", formatProvider);
			Assert.assertEquals(v.floatValue(), 12345.67, 0.001);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_null() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			Float val = floatFormatter.parse(null, formatProvider);
			Assert.assertNull(val);

			val = floatFormatter.parse("", formatProvider);
			Assert.assertNull(val);
		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Test
	public void test_invalid() {
		FormatProvider formatProvider = new FormatProviderImpl(Language.EN);
		try {
			floatFormatter.parse("12-345", formatProvider);
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof FormatterException);
		}
	}
}
