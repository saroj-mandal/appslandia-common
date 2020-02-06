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

import com.appslandia.common.base.Params;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class URLUtilsTest {

	@Test
	public void test_toQueryParams() {
		Params params = new Params().put("p1", "val1").put("p2", "val 2");
		String queryString = URLUtils.toQueryParams(params);

		Assert.assertTrue(queryString.contains("p1=val1"));
		Assert.assertTrue(queryString.contains("p2=val+2"));
	}

	@Test
	public void test_toQueryParams_arrayValue() {
		Params params = new Params().put("p1", new String[] { "val11", "val12" }).put("p2", "val2");
		String queryString = URLUtils.toQueryParams(params);

		Assert.assertTrue(queryString.contains("p1=val11&p1=val12"));
		Assert.assertTrue(queryString.contains("p2=val2"));

		params = new Params().put("p1", new String[] { "val11", null }).put("p2", "val2");
		queryString = URLUtils.toQueryParams(params);

		Assert.assertTrue(queryString.contains("p1=val11&p1="));
		Assert.assertTrue(queryString.contains("p2=val2"));
	}

	@Test
	public void test_toURL() {
		String url = URLUtils.toUrl("/app/index.html", new Params().put("p1", "val1"));
		Assert.assertTrue(url.contains("/app/index.html?p1=val1"));

		url = URLUtils.toUrl("http://server/app/index.html", new Params().put("p1", "val1"));
		Assert.assertTrue(url.contains("http://server/app/index.html?p1=val1"));

		url = URLUtils.toUrl("/app/index.html?p0=val0", new Params().put("p1", "val1"));
		Assert.assertTrue(url.contains("/app/index.html?p0=val0&p1=val1"));

		url = URLUtils.toUrl("http://server/app/index.html?p0=val0", new Params().put("p1", "val1"));
		Assert.assertTrue(url.contains("http://server/app/index.html?p0=val0&p1=val1"));
	}
}
