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

package com.appslandia.common.validators;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.MathUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class BitMaskTest {

	@Test
	public void test() {
		TestModel m = new TestModel();

		m.value = 0;
		Set<?> errors = ValidatorUtils.getValidator().validate(m);
		Assert.assertTrue(errors.isEmpty());

		m.value = 1;
		errors = ValidatorUtils.getValidator().validate(m);
		Assert.assertTrue(errors.isEmpty());

		m.value = 7;
		errors = ValidatorUtils.getValidator().validate(m);
		Assert.assertTrue(errors.isEmpty());
	}

	@Test
	public void test_invalid() {
		TestModel m = new TestModel();

		m.value = -1;
		Set<?> errors = ValidatorUtils.getValidator().validate(m);
		Assert.assertTrue(!errors.isEmpty());

		m.value = MathUtils.pow2(3);
		errors = ValidatorUtils.getValidator().validate(m);
		Assert.assertTrue(!errors.isEmpty());
	}

	private static class TestModel {

		// 1,2,4
		@BitMask(3)
		public long value;
	}
}
