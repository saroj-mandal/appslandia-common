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

package com.appslandia.common.base;

import org.junit.Assert;
import org.junit.Test;

import com.appslandia.common.utils.CharUtils;
import com.appslandia.common.utils.URLEncoding;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class URLEncodingTest {

	@Test
	public void test_param() {
		String alphanum = new String(CharUtils.toCharRanges("a-zA-Z0-9"));
		String s = alphanum + new String(new char[] { '-', '.', '_', '*' });

		String enc = URLEncoding.encodeParam(s);
		String dec = URLEncoding.decodeParam(enc);

		Assert.assertEquals(enc, s);
		Assert.assertEquals(dec, s);
	}

	@Test
	public void test_path() {
		String alphanum = new String(CharUtils.toCharRanges("a-zA-Z0-9"));

		// @formatter:off
		String s = alphanum 
				+ new String(new char[] { '-', '.', '_', '~' }) 
				+ new String(new char[] { ':', '@' }) 
				+ new String(new char[] { '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=' });
		// @formatter:on

		String enc = URLEncoding.encodePath(s);
		String dec = URLEncoding.decodePath(enc);

		Assert.assertEquals(enc, s);
		Assert.assertEquals(dec, s);
	}

	@Test
	public void test_pathXmlSafe() {

		String alphanum = new String(CharUtils.toCharRanges("a-zA-Z0-9"));

		// @formatter:off
		String s = alphanum 
				+ new String(new char[] { '-', '.', '_', '~' }) 
				+ new String(new char[] { ':', '@' }) 
				+ new String(new char[] { '!', '$', '(', ')', '*', '+', ',', ';', '=' });
		// @formatter:on

		String enc = URLEncoding.encodePathXmlSafe(s);
		String dec = URLEncoding.decodePath(enc);

		Assert.assertEquals(enc, s);
		Assert.assertEquals(dec, s);

		s = new String(new char[] { '&', '\'' });
		enc = URLEncoding.encodePathXmlSafe(s);
		dec = URLEncoding.decodePath(enc);

		Assert.assertEquals(enc, "%26%27");
		Assert.assertEquals(dec, s);
	}
}
