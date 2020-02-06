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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class MathUtilsTest {

	@Test
	public void test_toByteArray() {
		byte[] bytes = MathUtils.toByteArray(100);
		Assert.assertEquals(MathUtils.toInt(bytes), 100);

		bytes = MathUtils.toByteArray(1000L);
		Assert.assertEquals(MathUtils.toLong(bytes), 1000L);
	}

	@Test
	public void test_ceil() {
		Assert.assertEquals(MathUtils.ceil(0, 15), 0);

		for (int n = 1; n < 15; n++) {
			Assert.assertEquals(MathUtils.ceil(n, 15), 15);
		}
		Assert.assertEquals(MathUtils.ceil(16, 15), 30);
	}

	@Test
	public void test_pow2() {
		Assert.assertEquals(1, MathUtils.pow2(0));
		Assert.assertEquals(2, MathUtils.pow2(1));
		Assert.assertEquals(4, MathUtils.pow2(2));
	}
}
