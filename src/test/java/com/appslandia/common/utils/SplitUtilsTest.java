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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SplitUtilsTest {

	@Test
	public void test_splitByComma() {

		List<String> items = SplitUtils.splitByComma("abc , def, \r\nghk");
		Assert.assertTrue(items.size() == 3);

		Assert.assertEquals(items.get(0), "abc");
		Assert.assertEquals(items.get(1), "def");
		Assert.assertEquals(items.get(2), "ghk");
	}

	@Test
	public void test_splitByCommaOrSpace() {

		List<String> items = SplitUtils.splitByCommaOrSpace("abc,, \r\n,,\t def \t,,\t\t\tghk");
		Assert.assertTrue(items.size() == 3);

		Assert.assertEquals(items.get(0), "abc");
		Assert.assertEquals(items.get(1), "def");
		Assert.assertEquals(items.get(2), "ghk");
	}

	@Test
	public void test_splitByLineSep() {

		List<String> items = SplitUtils.splitByLineSep("abc \r\n\t def \r\n\t\t ghk");
		Assert.assertTrue(items.size() == 3);

		Assert.assertEquals(items.get(0), "abc");
		Assert.assertEquals(items.get(1), "def");
		Assert.assertEquals(items.get(2), "ghk");
	}

	@Test
	public void test_split() {

		List<String> items = SplitUtils.split("abc - def - ghk", '-');
		Assert.assertTrue(items.size() == 3);

		Assert.assertEquals(items.get(0), "abc");
		Assert.assertEquals(items.get(1), "def");
		Assert.assertEquals(items.get(2), "ghk");
	}
}
